package cn.hangcc.automaticclockinxust.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalDateUtils {

    public static final DateTimeFormatter LEGACY_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter INT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter LEGACY_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static final DateTimeFormatter INT_YEAR_MONTH_DAY_HOUR_MINUTE = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    /**
     * LocalDataTime转为可读的时间
     * @param localDateTime
     * @return
     */
    public static String getLocalDateTimeStr(LocalDateTime localDateTime) {
        return LEGACY_DATE_TIME_FORMATTER.format(localDateTime);
    }

    /**
     * 获取当前时间的可读形式
     * @return
     */
    public static String getNowTime() {
        return getLocalDateTimeStr(LocalDateTime.now());
    }

    /**
     * 获取当前的日期 yyyy-MM-dd
     * @return
     */
    public static String getTodayDate() {
        return DATE_FORMATTER.format(LocalDateTime.now());
    }

    /**
     * 获取下一天的日期 yyyy-MM-dd
     */
    public static String getNextDate() {
        return DATE_FORMATTER.format(LocalDateTime.now().plusDays(1));
    }

    /**
     * 获取小时 分钟 秒
     * @param target 待转换的字符串
     * @return LocalTime
     */
    public static LocalTime getLocalTime(String target) {
        return LocalTime.parse(target, LEGACY_TIME_FORMATTER);
    }

    /**
     * LocalDate to yyyyMMdd(int)
     */
    public static int getInt(LocalDate localDate) {
        return Integer.valueOf(localDate.format(INT_DATE_FORMATTER));
    }

    /**
     * LocalDateTime to yyyyMMddHHmm(long)
     */
    public static long getLong(LocalDateTime dateTime) {
        return Long.valueOf(dateTime.format(INT_YEAR_MONTH_DAY_HOUR_MINUTE));
    }
}
