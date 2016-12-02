package util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zsq on 16/12/2.
 */
public class ObjectUtils {

    /**
     * 如果为null返回-1
     * @param object
     * @return
     */
    public static Integer castInteger(Object object) {
        if (object == null) {
            return 0;
        } else {
            return (Integer)object;
        }
    }

    /**
     * 如果为null返回空字符串
     * @param object
     * @return
     */
    public static String castString(Object object) {
        if (object == null) {
            return "";
        } else {
            return object.toString();
        }
    }

    /**
     * 如果为null返回0
     * @param object
     * @return
     */
    public static BigInteger castBigInteger(Object object) {
        if (object == null) {
            return new BigInteger("0");
        } else {
            return (BigInteger) object;
        }

    }

    public static Boolean castBoolean(Object object) {
        if (object == null) {
            return false;
        } else {
            return (Boolean) object;
        }
    }

    public static Integer getInterFromBigDecimal(Object object) {
        if (object == null) {
            return 0;
        } else {
            return ((BigDecimal)object).intValue();
        }
    }

    public static Double getDoubleFromBigDecimal(Object object) {
        if (object == null) {
            return 0d;
        } else {
            return ((BigDecimal)object).doubleValue();
        }
    }

    public static Double getDoubleFromInteger(Object object) {
        if (object == null) {
            return 0d;
        } else {
            return ((Integer)object).doubleValue();
        }
    }

    public static Double getDoubleFromBigInteger(Object object) {
        if (object == null) {
            return 0d;
        } else {
            return ((BigInteger)object).doubleValue();
        }
    }

    public static Long castLong(Object obj){
        if (obj == null) {
            return 0L;
        } else {
            return (Long)obj;
        }
    }

    public static Long getLongFromInteger(Object obj) {
        if (obj == null) {
            return 0L;
        } else {
            return ((Integer)obj).longValue();
        }
    }

    public static Long getLongFromBigDecimal(Object obj) {
        if (obj == null) {
            return 0L;
        } else {
            return ((BigDecimal)obj).longValue();
        }
    }

    public static Long getLongFromBigInteger(Object obj) {
        if (obj == null) {
            return 0L;
        } else {
            return ((BigInteger)obj).longValue();
        }
    }

    public static Timestamp castTimestamp(Object obj) {
        if (obj == null) {
            return null;
        } else {
            return (Timestamp)obj;
        }
    }

    public static byte castByte(Object obj) {
        if (obj == null) {
            return 0;
        } else {
            return (byte)obj;
        }
    }

    public static Integer getIntegerFromBigInteger(Object object) {
        if (object == null) {
            return 0;
        } else {
            return ((BigInteger)object).intValue();
        }
    }

    public static Set<Integer> getIntegerSet(Integer ... args) {
        Set<Integer> result = new HashSet<Integer>();
        for (Integer integer : args) {
            result.add(integer);
        }
        return result;
    }

    public static Boolean getBooleanFromCharacter(Object object) {
        if (object == null) {
            return false;
        }
        Character character = (Character) object;
        if (character.toString().equals("1")) {
            return true;
        } else {
            return false;
        }
    }
}
