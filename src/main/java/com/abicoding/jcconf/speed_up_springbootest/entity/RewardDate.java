package com.abicoding.jcconf.speed_up_springbootest.entity;

import com.abicoding.jcconf.speed_up_springbootest.util.TimeUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RewardDate {

    private final Integer value;

    public static RewardDate create(Instant instant) {
        Integer dateValue = TimeUtils.toYYYYMMDD(instant);
        return new RewardDate(dateValue);
    }

    public static RewardDate restore(Integer value) {
        return new RewardDate(value);
    }
}