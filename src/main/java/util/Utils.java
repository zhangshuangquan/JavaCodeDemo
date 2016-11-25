package util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zsq on 16/11/25.
 */
public class Utils {

    private static final String RANDOM62 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String sha1(String input) {
        String output = null;
        try {
            byte[] inputBytes = input.getBytes();
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(inputBytes);
            byte[] outputBytes = sha.digest();
            //Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < outputBytes.length; i++) {
                hexString.append(String.format("%02X", 0xFF & outputBytes[i]));
            }
            output = hexString.toString();
        } catch (Exception e) {

        } finally {
            return output.toLowerCase();
        }
    }

    public static String md5(String input) {
        String output = null;
        try {
            byte[] inputBytes = input.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(inputBytes);
            byte[] outputBytes = md5.digest();
            //Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < outputBytes.length; i++) {
                hexString.append(String.format("%02X", 0xFF & outputBytes[i]));
            }
            output = hexString.toString();
        } catch (Exception ex) {
        } finally {
            return output != null ? output.toUpperCase() : output;
        }
    }

    public static DateTime getByTimestamp(Long ticks) {
        if (ticks == null) {
            return null;
        }
        Timestamp timestamp = new Timestamp(ticks);
        DateTime time = new DateTime(timestamp.getTime());
        return time;
    }

    public static String getRandomStr62(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(RANDOM62.length());
            sb.append(RANDOM62.charAt(number));
        }
        return sb.toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 校验电话号码是否合法
     *
     * @author kxw
     * @time 11/19/15 13:52
     */
    public static boolean validPhone(String fixedPhone) {

        boolean isValid = false;

        //判空, 为空直接返回无效
        if (fixedPhone == null || fixedPhone.isEmpty()) {
            return true;
        }

        //正则匹配, 8位数字, 11位数字, 12位数字, 6为数字
        Pattern p = Pattern.compile("^((\\d{8})|(\\d{11})|(\\d{12})|(\\d{6}))$");
        try {
            Matcher m = p.matcher(fixedPhone);
            isValid = m.matches();
        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }

    public static boolean validIdNo(String fixIdNo) {
        boolean isValid = false;
        if(fixIdNo == null || fixIdNo.isEmpty()) {
            return true;
        }
        Pattern p = Pattern.compile("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$");
        try {
            Matcher m = p.matcher(fixIdNo);
            isValid = m.matches();
        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * get today's date's number representation(e.g. "2014-07-24" to 20140724)
     */
    public static Integer getTodayDateNumberRepr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(df.format(new Date()));
    }

    public static Integer getDateNumberRepr(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(df.format(date));
    }

    public static String getTodayDate(String dateFormat) {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(new Date());
    }

    public static Integer getYesterdayDateNumberReor() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(df.format(new Date(new Date().getTime() - 24*60*60*1000)));
    }

    public static String getYesterdayDate(String dateFormat) {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(new Date(new Date().getTime() - 24*60*60*1000));
    }

    public static String toDateStr(Long ticks, SimpleDateFormat simpleDateFormat) {
        Date date = new Date(ticks);
        return simpleDateFormat.format(date);
    }

    public static String getSQLPlacedParameters(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("?,");
        }

        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    public static String getIP() {
        String localip = null;
        String netip = null;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            InetAddress ip = null;
            boolean finded = false;
            while (networkInterfaces.hasMoreElements() && !finded) {
                NetworkInterface ni = networkInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = address.nextElement();
                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                        netip = ip.getHostAddress();
                    } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                        localip = ip.getHostAddress();
                        finded = true;
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return localip;
    }

    public static Map<String, Object> transBean2Map(Object obj) {

        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }

    public static String encrypt(String bef_aes, String password) {
        byte[] byteContent = null;
        try {
            byteContent = bef_aes.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encrypt(byteContent, password);
    }

    public static String encrypt(byte[] content, String password) {
        try {
            SecretKey secretKey = getKey(password);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            String aft_aes = parseByte2HexStr(result);
            return aft_aes; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String aft_aes, String password) {
        try {
            byte[] content = parseHexStr2Byte(aft_aes);
            SecretKey secretKey = getKey(password);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            String bef_aes = new String(result);
            return bef_aes; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int value = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 2), 16);
            result[i] = (byte) value;
        }
        return result;
    }

    public static SecretKey getKey(String strKey) {
        try {
            KeyGenerator _generator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strKey.getBytes());
            _generator.init(128, secureRandom);
            return _generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("初始化密钥出现异常");
        }
    }

    public static Boolean isWxBrowser(String userAgent) {
        return StringUtils.lowerCase(userAgent).contains("micromessenger");
    }

    public static Double getBigDecimalOne(Double d) {
        BigDecimal t = new BigDecimal(d + "");
        return t.setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();
    }

    public static String getWxUrl(String fullUrl, String appId) {
        StringBuffer oauthUrl = new StringBuffer(100);
        oauthUrl.append("https://open.weixin.qq.com/connect/oauth2/")
                .append("authorize?appid=")
                .append(appId).append("&redirect_uri=")
                .append(fullUrl)
                .append("&response_type=code&scope=snsapi_base&state=huge#wechat_redirect");
        return oauthUrl.toString();
    }

    public static String getWxUrlUserInfoScope(String fullUrl, String appId) {
        StringBuffer oauthUrl = new StringBuffer(100);
        oauthUrl.append("https://open.weixin.qq.com/connect/oauth2/")
                .append("authorize?appid=")
                .append(appId).append("&redirect_uri=")
                .append(fullUrl)
                .append("&response_type=code&scope=snsapi_userinfo&state=huge#wechat_redirect");
        return oauthUrl.toString();
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss
     *
     * @return String
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }

    /**
     * 取出一个指定长度大小的随机正整数.
     *
     * @param length
     *            int 设定所取出随机数的长度。length小于11
     * @return int 返回生成的随机数。
     */
    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    public static String generateOrderCode(String prefix) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String strTime = sdf.format(new Date());
        /** 四位随机数 */
        String strRandom = Utils.buildRandom(4) + "";
        String orderCode = String.format("%s%s%s", prefix, strTime, strRandom);
        return orderCode;
    }
}
