package tool;

import wordAna.Token;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class ReadTool {
    public static ArrayList<Character> read(String fileName) throws Exception{
        ArrayList<Character> chars = new ArrayList<>();
        int oneChar;
        FileReader fileReader = new FileReader(new File(fileName));
        oneChar = fileReader.read();
        while (oneChar != -1) {
            chars.add((char)oneChar);
            oneChar = fileReader.read();
        }
        return chars;
    }
    public static boolean stringComp(String a, String b) {
        return Objects.equals(a.toUpperCase(), b.toUpperCase());
    }

}
