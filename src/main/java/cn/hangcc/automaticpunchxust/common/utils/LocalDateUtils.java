package cn.hangcc.automaticpunchxust.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateUtils {

    public static final DateTimeFormatter LEGACY_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * LocalDataTime转为可读的时间
     * @param localDateTime
     * @return
     */
    public static String getLocalDateTimeStr(LocalDateTime localDateTime) {
        return LEGACY_DATE_TIME_FORMATTER.format(localDateTime);
    }

    public static String getNowTime() {
        return getLocalDateTimeStr(LocalDateTime.now());
    }
}
