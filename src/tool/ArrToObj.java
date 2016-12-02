package tool;

import java.util.ArrayList;

/**
 * Created by Yuriko on 2016/11/30.
 */
public class ArrToObj {
    public static String charArrToString(ArrayList<Character> chars) {
        String result = "";
        for (Character aChar : chars) {
            result += aChar;
        }
        return result;
    }
    public static double charArrToDouble(ArrayList<Character> chars) {
        String result = "";
        for (Character aChar : chars) {
            result += aChar;
        }
        return Double.parseDouble(result);
    }
}
