package com.spochi.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtil {

    public static long dateToMilliUTC(LocalDateTime date) {
        return date.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
    }

    public static LocalDateTime milliToDateUTC(long milli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneId.of("UTC"));
    }
}
