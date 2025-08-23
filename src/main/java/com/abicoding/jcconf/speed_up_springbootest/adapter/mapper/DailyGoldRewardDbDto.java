package com.abicoding.jcconf.speed_up_springbootest.adapter.mapper;

import lombok.Data;

@Data
public class DailyGoldRewardDbDto {
    private Long id;
    private Long userId;
    private Integer rewardDate;
    private Long amount;
    private Long createdAt;
}