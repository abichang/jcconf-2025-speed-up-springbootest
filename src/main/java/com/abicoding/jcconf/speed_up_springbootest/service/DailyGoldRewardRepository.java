package com.abicoding.jcconf.speed_up_springbootest.service;

import java.time.Instant;

public interface DailyGoldRewardRepository {
    boolean hasClaimed(Long userId, Instant date);
    void createReward(Long userId, Instant date, Long amount);
}