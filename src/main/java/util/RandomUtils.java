package util;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by zsq on 16/12/7.
 */
public class RandomUtils {

    private static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTERCHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERCHAR = "0123456789";

    /**
     * 返回一个定长的随机字符串(包含数字和大小写字母)
     *
     * @param length
     *            随机数的长度
     * @return
     */
    public static String generateString(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯数字字符串(只包含数字)
     *
     * @param length
     *            随机数的长度
     * @return
     */
    public static String generateStringByNumberChar(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBERCHAR.charAt(random.nextInt(NUMBERCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)
     *
     * @param length
     *            随机数的长度
     * @return
     */
    public static String generateStringByLetterCharr(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(LETTERCHAR.charAt(random.nextInt(LETTERCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯小写字母字符串(只包含英文写字母)
     *
     * @param length
     *            随机数的长度
     * @return
     */
    public static String generateLowerString(int length) {
        return generateStringByLetterCharr(length).toLowerCase();
    }

    /**
     * 返回一个定长的随机纯大写字母字符串(只包含英文写字母)
     *
     * @param length
     *            随机数的长度
     * @return
     */
    public static String generateUpperString(int length) {
        return generateStringByLetterCharr(length).toUpperCase();
    }

    /**
     * 随机获取UUID字符串(无中划线)
     *
     * @return UUID字符串
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8) + uuid.substring(9, 13)
                + uuid.substring(14, 18) + uuid.substring(19, 23)
                + uuid.substring(24);
    }

    static Random ran = new Random();
    static SortedSet set = new TreeSet();

    public static void randomSet(int n, int m) {
        for (int i = 0; i < n; i++) {
            set.add(ran.nextInt(7) + 1);
        }
        if (set.size() < m) {
            randomSet(m - set.size(), m);
        }
    }



    public static void main(String[] args) {
        System.out.println(System.nanoTime());
        System.out.println(System.currentTimeMillis());

        randomSet(3, 3);
        System.out.print(set);
        System.out.println(" "+set.size());

        System.out.println(ran.nextInt(6)+1);
        System.out.println(ran.nextInt(6)+1);
        System.out.println(ran.nextInt(5)+1);

        int count = 0;
        int i = 0;
        int j = 0;
        int m = 0;
       do {
           i = ran.nextInt(10)+1;
           j = ran.nextInt(10)+1;
           m = ran.nextInt(10)+1;
           count = i+j+m;
       } while(count != 10);
        System.out.println(i);
        System.out.println(j);
        System.out.println(m);
    }
}
