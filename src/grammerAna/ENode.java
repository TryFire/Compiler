package grammerAna;

import wordAna.Scanner;

/**
 * Created by Yuriko on 2016/12/3.
 */
public class ENode {
    Scanner.Token_Type type;

    ENode left;
    ENode right;

    ENode child;
    String mathFunName;

    double caseConst;
    MyDouble caseParm;

    public ENode(Scanner.Token_Type type) {
        this.type = type;
    }

    public static ENode createOperatorENode(Scanner.Token_Type token_type, ENode left, ENode right) {
        ENode node = new ENode(token_type);
        node.setLeft(left);
        node.setRight(right);
        return node;
    }

    public static ENode createFuncENode(Scanner.Token_Type token_type, ENode child, String mathFunName) {
        ENode node = new ENode(token_type);
        node.setChild(child);
        node.setMathFunName(mathFunName);
        return node;
    }

    public static ENode createConst(Scanner.Token_Type token_type, double value) {
        ENode node = new ENode(token_type);
        node.setCaseConst(value);
        return node;
    }

    public Scanner.Token_Type getType() {
        return type;
    }

    public void setType(Scanner.Token_Type type) {
        this.type = type;
    }

    public ENode getLeft() {
        return left;
    }

    public void setLeft(ENode left) {
        this.left = left;
    }

    public ENode getRight() {
        return right;
    }

    public void setRight(ENode right) {
        this.right = right;
    }

    public ENode getChild() {
        return child;
    }

    public void setChild(ENode child) {
        this.child = child;
    }

    public String getMathFunName() {
        return mathFunName;
    }

    public void setMathFunName(String mathFunName) {
        this.mathFunName = mathFunName;
    }

    public double getCaseConst() {
        return caseConst;
    }

    public void setCaseConst(double caseConst) {
        this.caseConst = caseConst;
    }

    public MyDouble getCaseParm() {
        return caseParm;
    }

    public void setCaseParm(MyDouble caseParm) {
        this.caseParm = caseParm;
    }
}
