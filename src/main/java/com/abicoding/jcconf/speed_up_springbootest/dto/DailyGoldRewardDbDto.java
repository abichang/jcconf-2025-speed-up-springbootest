package com.abicoding.jcconf.speed_up_springbootest.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DailyGoldRewardDbDto {
    private Long id;
    private Long userId;
    private LocalDate rewardDate;
    private Long amount;
    private Long createdAt;
}