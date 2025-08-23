package com.abicoding.jcconf.speed_up_springbootest.service;

import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
import com.abicoding.jcconf.speed_up_springbootest.entity.RewardDate;
import com.abicoding.jcconf.speed_up_springbootest.entity.User;

public interface DailyGoldRewardRepository {
    boolean hasClaimed(User user, RewardDate rewardDate);
    void claim(DailyGoldReward reward);
}