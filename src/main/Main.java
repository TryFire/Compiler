package main;

import wordAna.Token;
import java.util.ArrayList;

/**
 * Created by Yuriko on 2016/11/30.
 */
public class Main {
    public static final String FILE_NAME = "G:\\Users\\function\\IdealWorkspace\\Complier\\src\\main\\draw.txt";
    public static void main(String[] args) {
        ArrayList<Token> tokens;
        wordAna.Scanner scanner = new wordAna.Scanner();
        tokens = scanner.run(FILE_NAME);
        for (int i = 0; i < tokens.size(); i++) {
            tokens.get(i).display();
        }
        System.out.print("total lines:" + scanner.getLines());
    }
}
