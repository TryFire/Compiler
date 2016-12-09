package grammerAna;

import meanAna.Compter;
import wordAna.Scanner;
import wordAna.Token;

import java.util.ArrayList;

/**
 * Created by Yuriko on 2016/12/7.
 */
public class Parser {
    private ArrayList<Token> tokens = null;
    private int currIndex;
    private String funcName;
    private double caseConst;
    public double caseT = 0.0;
    private Token token;
    private Compter compter;
    public void run(ArrayList<Token> tokens, Compter c) {
        compter = c;
        initParser(tokens);
        program();
    }
    private void initParser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        currIndex = 0;
    }
    private void fetchToken() {
        if (tokens != null) {
            if (currIndex == tokens.size()) {
                System.out.println("end");
                token = null;
            } else {
                token = tokens.get(currIndex);
                currIndex++;
                if (token.getType() == Scanner.Token_Type.ERRTOKEN) {
                    //
                    System.out.println("error type");
                }
            }
        }
    }
    private ENode makeExprNode(Scanner.Token_Type type, ENode... node1) {
        ENode node = new ENode(type);
        switch (type) {
            case FUNC:
                node.setChild(node1[1]);
                node.setMathFunName(funcName);
                break;
            case CONST_ID:
                node.setCaseConst(caseConst);
                break;
            case T:
                node.setCaseParm(compter.parameter);
                break;
            default:
                node.setLeft(node1[0]);
                node.setRight(node1[1]);
                break;
        }
        return node;
    }

    private void program() {
        fetchToken();
        enter("Program");
        while (token != null) {
            statement();
            matchToken(Scanner.Token_Type.SEMICO);
        }
        back("Program");
    }

    private void matchToken(Scanner.Token_Type token_type) {
        if (token.getType() != token_type) {
            error(1);
        }
        fetchToken();
    }

    private void callMatch(Scanner.Token_Type s) {
        System.out.println("match token      " + s);
    }

    private void statement() {
        enter("Statement");
        switch (token.getType()) {
            case ORIGIN:
                originStatement();
                break;
            case ROT:
                rotStatement();
                break;
            case SCALE:
                scaleStatement();
                break;
            case FOR:
                forStatement();
                break;
            default:
                error(0);
                break;
        }
        back("Statement");
    }

    private void originStatement() {
        enter("OriginStatement");
        ENode node;
        matchToken(Scanner.Token_Type.ORIGIN);
        matchToken(Scanner.Token_Type.IS);
        matchToken(Scanner.Token_Type.L_BRACKET);
        node = expression();
        compter.setOriginX(Compter.getExprValue(node));
        matchToken(Scanner.Token_Type.COMMA);
        node = expression();
        compter.setOriginY(Compter.getExprValue(node));
        matchToken(Scanner.Token_Type.R_BRACKET);
        back("OriginStatement");
    }

    private void rotStatement() {
        enter("RotStatement");
        ENode node;
        matchToken(Scanner.Token_Type.ROT);
        matchToken(Scanner.Token_Type.IS);
        node = expression();
        compter.setRotAngle(Compter.getExprValue(node));
        back("RotStatement");
    }

    private void scaleStatement() {
        enter("ScaleStatement");
        ENode node;
        matchToken(Scanner.Token_Type.SCALE);
        matchToken(Scanner.Token_Type.IS);
        matchToken(Scanner.Token_Type.L_BRACKET);
        node = expression();
        compter.setScaleX(Compter.getExprValue(node));
        matchToken(Scanner.Token_Type.COMMA);
        node = expression();
        compter.setScaleY(Compter.getExprValue(node));
        matchToken(Scanner.Token_Type.R_BRACKET);
        back("ScaleStatement");
    }

    private void forStatement() {
        enter("ForStatement");
        ENode nodeStart,nodeEnd, nodeStep, nodeX, nodeY;
        matchToken(Scanner.Token_Type.FOR);
        callMatch(Scanner.Token_Type.FOR);
        matchToken(Scanner.Token_Type.T);
        callMatch(Scanner.Token_Type.T);
        matchToken(Scanner.Token_Type.FROM);
        callMatch(Scanner.Token_Type.FROM);
        nodeStart = expression();

        matchToken(Scanner.Token_Type.TO);
        callMatch(Scanner.Token_Type.TO);
        nodeEnd = expression();

        matchToken(Scanner.Token_Type.STEP);
        callMatch(Scanner.Token_Type.STEP);
        nodeStep = expression();

        matchToken(Scanner.Token_Type.DRAW);
        callMatch(Scanner.Token_Type.DRAW);
        matchToken(Scanner.Token_Type.L_BRACKET);
        callMatch(Scanner.Token_Type.L_BRACKET);
        nodeX = expression();
        matchToken(Scanner.Token_Type.COMMA);
        nodeY = expression();
        matchToken(Scanner.Token_Type.R_BRACKET);
        callMatch(Scanner.Token_Type.R_BRACKET);
        back("ForStatement");

        double start = Compter.getExprValue(nodeStart);
        double end = Compter.getExprValue(nodeEnd);
        double step = Compter.getExprValue(nodeStep);

        compter.drawLoop(start, end, step, nodeX, nodeY);

    }

    private ENode expression() {
        ENode nodeLeft, nodeRight;
        Scanner.Token_Type typeTemp;

        enter("expression");
        nodeLeft = term();
        while (token.getType() == Scanner.Token_Type.PLUS || token.getType() == Scanner.Token_Type.MINUS) {
            typeTemp = token.getType();
            matchToken(typeTemp);
            nodeRight = term();
            nodeLeft = ENode.createOperatorENode(typeTemp, nodeLeft, nodeRight);
        }
        printTree(nodeLeft);
        back("expression");
        return nodeLeft;
    }

    private ENode term() {
        ENode nodeLeft, nodeRight;
        Scanner.Token_Type typeTemp;

        nodeLeft = factor();
        while (token.getType() == Scanner.Token_Type.MUL || token.getType() == Scanner.Token_Type.DIV) {
            typeTemp = token.getType();
            matchToken(typeTemp);
            nodeRight = factor();
            nodeLeft = ENode.createOperatorENode(typeTemp, nodeLeft, nodeRight);
        }
        return nodeLeft;
    }

    private ENode factor() {
        ENode nodeLeft, nodeRight;

        if (token.getType() == Scanner.Token_Type.PLUS) {
            matchToken(Scanner.Token_Type.PLUS);
            nodeRight = factor();
        }else if (token.getType() == Scanner.Token_Type.MINUS) {
            matchToken(Scanner.Token_Type.MINUS);
            nodeRight = factor();
            nodeLeft = new ENode(Scanner.Token_Type.CONST_ID);
            nodeLeft.setCaseConst(0.0);
            nodeRight = ENode.createOperatorENode(Scanner.Token_Type.MINUS, nodeLeft, nodeRight);
        } else {
            nodeRight = component();
        }

        return nodeRight;
    }

    private ENode component() {
        ENode nodeLeft, nodeRight;
        nodeLeft = atom();
        if (token.getType() == Scanner.Token_Type.POWER) {
            matchToken(Scanner.Token_Type.POWER);
            nodeRight = component();
            nodeLeft = ENode.createOperatorENode(Scanner.Token_Type.POWER, nodeLeft, nodeRight);
        }
        return nodeLeft;
    }

    private ENode atom() {
        Token t = token;
        ENode address = null, temp;
        switch (token.getType()) {
            case CONST_ID:
                matchToken(Scanner.Token_Type.CONST_ID);
                address = ENode.createConst(Scanner.Token_Type.CONST_ID, t.getValue());
                break;
            case T:
                matchToken(Scanner.Token_Type.T);
                ENode node = new ENode(Scanner.Token_Type.T);
                node.setCaseParm(compter.parameter);
                address = node;
                break;
            case FUNC:
                matchToken(Scanner.Token_Type.FUNC);
                matchToken(Scanner.Token_Type.L_BRACKET);
                temp = expression();
                address = ENode.createFuncENode(Scanner.Token_Type.FUNC, temp, t.getFunName());
                matchToken(Scanner.Token_Type.R_BRACKET);
                break;
            case L_BRACKET:
                matchToken(Scanner.Token_Type.L_BRACKET);
                address = expression();
                matchToken(Scanner.Token_Type.R_BRACKET);
                break;
            default:
                error(1);
        }
        return address;
    }

    private void error(int a) {
        switch (a) {
            case 0:
                System.out.print("错误记号");
                break;
            case 1:
                System.out.print("不是预期记号");
                break;
            default:
                break;
        }

    }

    private void enter(String s) {
        System.out.println("enter in " + s);
    }

    private void back(String s) {
        System.out.println("exit from " + s);
    }

    private void printTree(ENode node) {
        tree(node, 1);
    }

    private void tree(ENode node, int temp){
        for (int i = 0; i < temp; i++) {
            System.out.print("\t");
        }
        switch (node.getType()) {
            case PLUS:
                System.out.println("+");
                break;
            case MINUS:
                System.out.println("-");
                break;
            case MUL:
                System.out.println("*");
                break;
            case DIV:
                System.out.println("/");
                break;
            case POWER:
                System.out.println("**");
                break;
            case CONST_ID:
                System.out.println(node.getCaseConst() + "");
                break;
            case T:
                System.out.println("T");
                break;
            case FUNC:
                System.out.println(node.getMathFunName() + "");
                break;
            default:
                System.out.println("error tree");
                break;
        }

        if (node.getType() == Scanner.Token_Type.CONST_ID || node.getType() == Scanner.Token_Type.T) {
            return;
        }
        if (node.getType() == Scanner.Token_Type.FUNC) {
            tree(node.getChild(), temp + 1);
        } else {
            tree(node.getLeft(), temp + 1);
            tree(node.getRight(), temp + 1);
        }
    }

}
