package com.abicoding.jcconf.speed_up_springbootest.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class DailyGoldReward {
    private Long id;
    private Long userId;
    private RewardDate rewardDate;
    private Long amount;
    private Instant createdAt;
}