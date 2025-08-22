package com.abicoding.jcconf.speed_up_springbootest.dto;

import lombok.Data;

@Data
public class DailyGoldRewardDbDto {
    private Long id;
    private Long userId;
    private Integer rewardDate;
    private Long amount;
    private Long createdAt;
}