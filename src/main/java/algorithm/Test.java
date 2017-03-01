package algorithm;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zsq on 16/12/11.
 */
public class Test {

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";


    public static void main(String[] args) {
        //键盘输入
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        Pattern regex = Pattern.compile(REGEX_EMAIL);
        Matcher matcher = regex.matcher(str);
        boolean isMatched = matcher.matches();
        if (isMatched) {
            System.out.println("1");
        } else{
            System.out.println("0");
        }
    }
}
