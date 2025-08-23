package com.abicoding.jcconf.speed_up_springbootest.service;

import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
import com.abicoding.jcconf.speed_up_springbootest.entity.RewardDate;

public interface DailyGoldRewardRepository {
    boolean hasClaimed(Long userId, RewardDate rewardDate);
    void claim(DailyGoldReward reward);
}