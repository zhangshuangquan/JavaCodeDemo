package util;

import model.DateTimeDiff;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zsq on 16/11/25.
 */
public class DateUtils {

    private static long dayMillions = 86400000;

    public static Date parserDate(DateTime dateTime) {
        return new Date(dateTime.getMillis());
    }

    public static DateTime parseDateTime(Date date) {
        DateTime dateTime = new DateTime(date.getTime());
        return dateTime;
    }

    public static Date parseDate(String strDate,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(strDate);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static Date parseDate(Timestamp timestamp) {
        return new Date(timestamp.getTime());
    }



    public static Date getTodayDateStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    public static Date getTodayDateEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,99);
        return calendar.getTime();
    }

    public static DateTime parseDateTime(String strDate,String format) {
        DateTimeFormatter dateFormat = DateTimeFormat.forPattern(format);
        return DateTime.parse(strDate,dateFormat);
    }

    public static DateTime getTodayDateTimeStart() {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return parseDateTime(sdf.format(getTodayDateStart()),format);
    }

    public static DateTime getTodayDateTimeEnd() {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return parseDateTime(sdf.format(getTodayDateEnd()),format);
    }

    public static Date getMondayDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        day = (day+6)%7;
        day = day==0?7:day;
        calendar.add(Calendar.DAY_OF_YEAR, 1-day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getSundayDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        day = (day+6)%7;
        day = day==0?7:day;
        calendar.add(Calendar.DAY_OF_YEAR, 7-day);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static DateTime getSundayDateTime(Date date) {
        return parseDateTime(getSundayDate(date));
    }

    public static DateTime getMondayDateTime(Date date) {
        return parseDateTime(getMondayDate(date));
    }

    public static Integer getDayInteger(Date date){
        if (date == null) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(sdf.format(date));
    }

    public static String getTimeStr(Date date,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getTimeStr(DateTime date,String format){
        return getTimeStr(parserDate(date),format);
    }

    public static long getTimeBetween(Date date1,Date date2){
        long time = date1.getTime() - date2.getTime();
        if (time < 0) {
            time = -time;
        }
        return time;
    }

    public static DateTimeDiff getDateTimeDiff(Date date1, Date date2) {
        long time = getTimeBetween(date1,date2);
        long dayTime = 24 * 60 * 60 * 1000;
        long hourTime = 60 * 60 * 1000;
        long minuteTime = 60 * 1000;
        Long l = time / dayTime;
        int day = l.intValue();
        time = time - day * dayTime;
        l = time / hourTime;
        int hour = l.intValue();
        time = time - hour * hourTime;
        l = time / minuteTime;
        int minute = l.intValue();
        return new DateTimeDiff(day,hour,minute);
    }

    public static Date getNextMonth(int day,int hour,int minute,int second,int milliSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,1);
        calendar.set(Calendar.DAY_OF_MONTH,day);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);
        calendar.set(Calendar.MILLISECOND,milliSecond);
        return calendar.getTime();
    }

    public static Date datePlus(Date date,int field,int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field,amount);
        return calendar.getTime();
    }

    public static int getMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static double getDaysBetween(Date startDate,Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        long millionsBetween = endDate.getTime() - startDate.getTime();
        return NumberUtils.roundHalfUp(MathUtils.divide(millionsBetween,dayMillions).doubleValue(),1);
    }
}
