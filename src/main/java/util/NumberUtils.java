package util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zsq on 16/11/25.
 */
public class NumberUtils {

    public static long transLong(double obj) {
        return new BigDecimal(obj).longValue();
    }

    public static double getDouble(double d,int scale,int roundType) {
        BigDecimal b = new BigDecimal(d + "");
        return b.setScale(scale,roundType).doubleValue();
    }

    public static double roundHalfUp(double obj) {
        BigDecimal b = new BigDecimal(obj + "");
        return b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double transGramToKilogram(Long gram) {
        return MathUtils.divide(gram,1000).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double transGramToKilogram(int gram) {
        return MathUtils.divide(gram,1000).setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double roundHalfUp(double obj,Integer scale) {
        BigDecimal b = new BigDecimal(obj + "");
        return b.setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Long transKilogramToGram(Double kilogram) {
        if (kilogram == null) {
            return 0l;
        } else {
            return MathUtils.multiply(kilogram,1000).longValue();
        }
    }

    public static int getIntVal(Double doubleVal) {
        if (doubleVal == null) {
            return 0;
        } else {
            return doubleVal.intValue();
        }
    }

    public static boolean isDouble(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        String reg = "^[-\\+]?\\d+(\\.\\d+)?$";
        Matcher matcher = Pattern.compile(reg).matcher(str);
        return matcher.matches();
    }

    public static boolean isInteger(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        String reg = "^\\d+$";
        Matcher matcher = Pattern.compile(reg).matcher(str);
        return matcher.matches();
    }
}
