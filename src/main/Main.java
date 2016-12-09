package main;

import grammerAna.Parser;
import meanAna.Compter;
import view.Compiler;
import wordAna.Token;

import java.util.ArrayList;

/**
 * Created by Yuriko on 2016/11/30.
 */
public class Main {
    public static final String FILE_NAME = "G:\\Users\\function\\IdealWorkspace\\Compiler\\src\\main\\draw.txt";

    public static void main(String[] args) {
        //new Main().run();
        Compiler compiler = new Compiler();
        compiler.lunchFrame();
    }




    public void run() {
        ArrayList<Token> tokens;
        wordAna.Scanner scanner = new wordAna.Scanner();
        tokens = scanner.run(FILE_NAME);
        /*System.out.printf("%10s%10s%10s%10s\n","类别", "原始输入", "值", "函数地址");
        for (int i = 0; i < tokens.size(); i++) {
            tokens.get(i).display();
        }
        System.out.print("total lines:" + scanner.getLines());*/
        Parser parser = new Parser();
        parser.run(tokens, new Compter());
    }
}
