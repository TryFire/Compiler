package wordAna;

import tool.ArrToObj;
import tool.ReadTool;

import java.util.ArrayList;

/**
 * Created by Yuriko on 2016/11/30.
 *
 * input:filename(read form file)
 * output:记号流(a list of Token)
 */
public class Scanner {
    //记号类型(types of word)
    public enum  Token_Type {
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

    private ArrayList<Token> tokenDic = new ArrayList<>();      //关键字记号字典
    private int lines;                                          //总行数
    private ArrayList<Character> buffers = new ArrayList<>();   //缓冲区 用来存放读取的字符但是没识别完全的
    private ArrayList<Character> oriCode;                       //原始读入的字符流
    private int currentCharNum;                                 //当前读取的字符
    private boolean isRun = false;                              //是否正在进行词法分析
    private boolean isFinished = false;                         //是否已结束当然的词法分析
    private boolean isInitDic = false;                          //是否已初始化字典

    /**
     * start of analyse 开始词法分析
     * @param fileName 文件名
     * @return a list of Token 返回记号流
     */
    public ArrayList<Token> run(String data) {
        isRun = true;
        initScanner(data);
        ArrayList<Token> restltToken = new ArrayList<>();
        while (getChar() != '\0') {
            backChar();
            Token token = getToken();

            restltToken.add(token);
        }
        isRun = false;
        isFinished = true;
        if (restltToken.get(restltToken.size()-1).getType() == Token_Type.NONTOKEN) {
            restltToken.remove(restltToken.size() - 1);
        }
        return restltToken;
    }

    /**
     * 初始化词法分析器 init...for Scanner
     * @param fileName
     */
    private void initScanner(String data) {
        lines = 0;
        currentCharNum = 0;
        isFinished = false;
        clearTokenBuffer();
        if (!isInitDic) {
            initTokenDic();
            isInitDic = true;
        }
        try {
            oriCode = ReadTool.readFromStr(data);
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
     * 从字符数组oriCode获取一个字符 get one char from oriCode
     * @return 返回一个字符
     */
    private char getChar() {
        if (currentCharNum == oriCode.size()) {
            return '\0';
        }
        char oneChar = oriCode.get(currentCharNum);
        currentCharNum++;
        return oneChar;
    }

    /**
     * 把当前读取的字符返回到源代码字符数组中
     */
    private void backChar() {
        currentCharNum--;
    }

    /**
     * 初始化（加载）关键字记号字典
     */
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

    /**
     * 识别出单词的字符数组与关键字字典进行比较
     * @param key 识别出的单词的字符数组
     * @return 如果与子弹匹配 返回字典中的几号 否则返回null
     */
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

    /**
     * 识别单词并且进行分析
     * @return 返回一个记号（Token）
     */
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

    /**
     * 获取总行数
     * @return 返回源代码的总行数
     */
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

    /**
     * 判断是否结束词法分析
     * @return 一结束返回true 否则返回false
     */
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
