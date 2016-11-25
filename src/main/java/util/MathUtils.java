package util;

import java.math.BigDecimal;

/**
 * Created by zsq on 16/11/25.
 */
public class MathUtils {


    /**
     * 含有double类型数据的加法运算
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal add(double d1, double d2){
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.add(b2);
    }

    /**
     * 含有double类型数据的减法运算
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal subtract(double d1, double d2){
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.subtract(b2);
    }

    /**
     * 含有double类型数据的乘法运算
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal multiply(double d1,double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.multiply(b2);
    }

    public static BigDecimal multiply(BigDecimal bigDecimal,double d) {
        BigDecimal b2 = new BigDecimal(Double.toString(d));
        return bigDecimal.multiply(b2);
    }

    /**
     * 含有double类型数据的除法运算
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal divide(double d1,double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.divide(b2,4,BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal divide(double d1,double d2,int scale) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal divide(BigDecimal bigDecimal,double d,int scale) {
        BigDecimal b = new BigDecimal(Double.toString(d));
        return bigDecimal.divide(b,scale,BigDecimal.ROUND_HALF_UP);
    }

    public static void main(String [] args) {
        System.out.println(MathUtils.divide(13.12, 9.27, 1));
    }
}
