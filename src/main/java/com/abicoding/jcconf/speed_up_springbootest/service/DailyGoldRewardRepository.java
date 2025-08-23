package com.abicoding.jcconf.speed_up_springbootest.service;

import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;

import java.time.Instant;

public interface DailyGoldRewardRepository {
    boolean hasClaimed(Long userId, Instant date);
    void claim(DailyGoldReward reward);
}