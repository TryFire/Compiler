package tool;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

public class ReadTool {
    /**
     * 从file读取字符
     * @param fileName 文件名
     * @return 返回字符数组
     * @throws Exception 文件不存在
     */
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

    public static ArrayList<Character> readFromStr(String data) {
        ArrayList<Character> chars = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            chars.add(data.charAt(i));
        }
        return chars;
    }
    public static boolean stringComp(String a, String b) {
        return Objects.equals(a.toUpperCase(), b.toUpperCase());
    }

}
