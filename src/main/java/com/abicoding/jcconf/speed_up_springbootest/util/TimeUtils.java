package com.abicoding.jcconf.speed_up_springbootest.util;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class TimeUtils {
    
    public Instant now() {
        return Instant.now();
    }
    
    public static Integer toYYYYMMDD(Instant instant) {
        return Integer.valueOf(instant.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }
}