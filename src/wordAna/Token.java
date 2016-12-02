package wordAna;

/**
 * Created by Yuriko on 2016/11/30.
 */
public class Token {
    private Scanner.Token_Type type;
    private String lexem;
    private double value;
    private String funName;

    public Token() {
        this.funName = "NULL";
        this.value = 0.0;
        this.lexem = "";
        this.type = Scanner.Token_Type.NONTOKEN;
    }

    public Token(Scanner.Token_Type type, String lexem, double value, String funName) {
        this.type = type;
        this.lexem = lexem;
        this.value = value;
        this.funName = funName;
    }

    public Scanner.Token_Type getType() {
        return type;
    }

    public void setType(Scanner.Token_Type type) {
        this.type = type;
    }

    public String getLexem() {
        return lexem;
    }

    public void setLexem(String lexem) {
        this.lexem = lexem;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public void display() {
        System.out.printf("%12s%12s%12f%12s\n",type, lexem, value, funName);
    }
}
