package wordAna;

import tool.ArrToObj;
import tool.ReadTool;

import java.util.ArrayList;

/**
 * Created by Yuriko on 2016/11/30.
 */
public class Scanner {
    protected enum  Token_Type {
        ORIGIN, SCALE, ROT, IS,                  // 保留字（一字一码)
        TO, STEP, DRAW,FOR, FROM,                // 保留字
        T,                                       // 参数
        ID,                                      // 变量
        SEMICO, L_BRACKET, R_BRACKET, COMMA,     // 分隔符
        PLUS, MINUS, MUL, DIV, POWER,            // 运算符
        FUNC,                                    // 函数（调用）
        CONST_ID,                                // 常数
        NONTOKEN,                                // 空记号（源程序结束)
        ERRTOKEN                                 // 出错记号（非法输入）
    }

    private ArrayList<Token> tokenDic = new ArrayList<>();      //记号字典
    private int lines;                                          //总行数
    private ArrayList<Character> buffers = new ArrayList<>();   //缓冲区 用来存放读取的字符但是没识别完全的
    private ArrayList<Character> oriCode;                       //原始读入的字符流
    private int currentCharNum;                                 //当前读取的字符
    private boolean isRun = false;                              //是否正在进行词法分析
    private boolean isFinished = false;                         //是否已结束当然的词法分析
    private boolean isInitDic = false;                          //是否已初始化字典

    public ArrayList<Token> run(String fileName) {
        isRun = true;
        initScanner(fileName);
        ArrayList<Token> restltToken = new ArrayList<>();
        while (getChar() != '\0') {
            backChar();
            Token token = getToken();

            restltToken.add(token);
        }
        isRun = false;
        isFinished = true;
        return restltToken;
    }

    private void initScanner(String fileName) {
        lines = 0;
        currentCharNum = 0;
        isFinished = false;
        clearTokenBuffer();
        if (!isInitDic) {
            initTokenDic();
            isInitDic = true;
        }
        try {
            oriCode = ReadTool.read(fileName);
        } catch (Exception e) {
            System.out.println("Can't find file");
        }
    }

    private void addTokenBuffer(char c) {
        buffers.add(c);
    }

    private void clearTokenBuffer() {
        buffers.clear();
    }

    /**
     * get one char
     * @return
     */
    private char getChar() {
        if (currentCharNum == oriCode.size()) {
            return '\0';
        }
        char oneChar = oriCode.get(currentCharNum);
        currentCharNum++;
        return oneChar;
    }

    private void backChar() {
        currentCharNum--;
    }

    private void initTokenDic() {
        tokenDic.add(new Token(Token_Type.CONST_ID, "PI", 3.1415926, "NULL"));
        tokenDic.add(new Token(Token_Type.CONST_ID, "E", 2.71828, "NULL"));
        tokenDic.add(new Token(Token_Type.T, "T", 0.0, "NULL"));
        tokenDic.add(new Token(Token_Type.FUNC, "SIN", 0.0, "sin"));
        tokenDic.add(new Token(Token_Type.FUNC, "COS", 0.0, "cos"));
        tokenDic.add(new Token(Token_Type.FUNC, "TAN", 0.0, "tan"));
        tokenDic.add(new Token(Token_Type.FUNC, "LN", 0.0, "log"));
        tokenDic.add(new Token(Token_Type.FUNC, "EXP", 0.0, "exp"));
        tokenDic.add(new Token(Token_Type.FUNC, "SQRT", 0.0, "sqrt"));
        tokenDic.add(new Token(Token_Type.ORIGIN, "ORIGIN", 0.0, "NULL"));
        tokenDic.add(new Token(Token_Type.SCALE, "SCALE", 0.0, "NULL"));
        tokenDic.add(new Token(Token_Type.ROT, "ROT", 0.0, "NULL"));
        tokenDic.add(new Token(Token_Type.IS, "IS", 0.0, "NULL"));
        tokenDic.add(new Token(Token_Type.FOR, "FOR", 0.0, "NULL"));
        tokenDic.add(new Token(Token_Type.FROM, "FROM", 0.0, "NULL"));
        tokenDic.add(new Token(Token_Type.TO, "TO", 0.0, "NULL"));
        tokenDic.add(new Token(Token_Type.STEP, "STEP", 0.0, "NULL"));
        tokenDic.add(new Token(Token_Type.DRAW, "DRAW", 0.0, "NULL"));
    }

    private Token judgeTokenDic(ArrayList<Character> key) {
        String keys = ArrToObj.charArrToString(key);
        Token token = null;
        for (int i = 0; i < tokenDic.size(); i++) {
            if (ReadTool.stringComp(tokenDic.get(i).getLexem(), keys)) {
                token = tokenDic.get(i);
                break;
            }
        }
        return token;
    }

    private Token getToken() {
        char oneChar;
        Token token = new Token();
        token.setLexem(ArrToObj.charArrToString(buffers));
        for (;;) {
            oneChar = getChar();
            if (oneChar == '\0') {
                token.setType(Token_Type.NONTOKEN);
 //               System.out.println("why non");
                clearTokenBuffer();
                return token;
            }
            if (oneChar == '\n')
                lines ++;
            if (!isSpase(oneChar))
                break;
        }
        addTokenBuffer(oneChar);
        if (isAlpha(oneChar)) { //if one char is alpha
            for (;;) {
                oneChar = getChar();
                if (isAlphaOrDigit(oneChar)) {
                    addTokenBuffer(oneChar);
                } else {
                    break;
                }
            }
            backChar();
            if (judgeTokenDic(buffers) != null) {
                Token token1 = judgeTokenDic(buffers);
                clearTokenBuffer();
                return token1;
            } else {
                token.setLexem(ArrToObj.charArrToString(buffers));
                token.setType(Token_Type.ID);
            }
            clearTokenBuffer();
            System.out.print(token.getType());
            return token;
        } else if (isDigit(oneChar)) { //if is digit
            boolean hasPoint = false;
            for (;;) {
                oneChar = getChar();
                if (isDigit(oneChar)) {
                    addTokenBuffer(oneChar);
                } else {
                    break;
                }
            }
            if (oneChar == '.') {
                addTokenBuffer(oneChar);
                for (;;) {
                    oneChar = getChar();
                    if (isDigit(oneChar)) {
                        addTokenBuffer(oneChar);
                    } else {
                        break;
                    }
                }
            }
            backChar();
            token.setType(Token_Type.CONST_ID);
            token.setLexem(ArrToObj.charArrToString(buffers));
            token.setValue(ArrToObj.charArrToDouble(buffers));
            clearTokenBuffer();
            return token;
        } else {
            switch (oneChar) {
                case ';':
                    token.setType(Token_Type.SEMICO);
                    break;
                case '(' :
                    token.setType(Token_Type.L_BRACKET);
                    break;
                case ')' :
                    token.setType(Token_Type.R_BRACKET);
                    break;
                case ',' :
                    token.setType(Token_Type.COMMA);
                    break;
                case '+' :
                    token.setType(Token_Type.PLUS);
                    break;
                case '-' :
                    oneChar = getChar();
                    if (oneChar == '-') { //if start with --
                        for (;;) {
                            oneChar = getChar();
                            if (oneChar == '\n') {
                                backChar();
                                break;
                            } else if (oneChar == '\0') {
                                return token;
                            }
                        }
                        clearTokenBuffer();
                        return getToken();
                    } else {               //if is -
                        backChar();
                        token.setType(Token_Type.MINUS);
                    }
                    break;
                case '*' :
                    oneChar = getChar();
                    if (oneChar == '*') {   //if is **(power)
                        addTokenBuffer(oneChar);
                        token.setType(Token_Type.POWER);
                        token.setLexem(ArrToObj.charArrToString(buffers));
                        clearTokenBuffer();
                        return token;
                    } else {                //if *
                        backChar();
                        token.setType(Token_Type.MUL);
                    }
                    break;
                case '/' :
                    oneChar = getChar();
                    if (oneChar == '/') { //if start with //
                        for (;;) {
                            oneChar = getChar();
                            if (oneChar == '\n') {
                                backChar();
                                break;
                            } else if (oneChar == '\0') {
                                return token;
                            }
                        }
                        clearTokenBuffer();
                        return getToken();
                    } else {              //if /
                        backChar();
                        token.setType(Token_Type.DIV);
                    }
                    break;
                default:
                    token.setType(Token_Type.ERRTOKEN);
                    break;
            }

        }
        token.setLexem(ArrToObj.charArrToString(buffers));
        clearTokenBuffer();
        return token;

    }

    public int getLines() {
        if (isRun) {
            System.out.println("analyse is not complete");
            return -1;
        }
        if (lines == 0) {
            return lines;
        }
        return lines + 1;
    }

    public boolean isFinished() {
        return isFinished;
    }

    private boolean isSpase(char c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t';
    }
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'z');
    }
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
    private boolean isAlphaOrDigit(char c) {
        return isAlpha(c) || isDigit(c);
    }
}
