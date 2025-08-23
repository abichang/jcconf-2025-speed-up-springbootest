package com.abicoding.jcconf.speed_up_springbootest.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    public static Integer toYYYYMMDD(Instant instant) {
        return Integer.valueOf(instant.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }
}