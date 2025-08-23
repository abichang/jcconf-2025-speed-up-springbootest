package com.abicoding.jcconf.speed_up_springbootest.adapter.repository;

import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.DailyGoldRewardDbDto;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.DailyGoldRewardMapper;
import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
import com.abicoding.jcconf.speed_up_springbootest.entity.RewardDate;
import com.abicoding.jcconf.speed_up_springbootest.entity.User;
import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldRewardRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class DailyGoldRewardRepositoryImpl implements DailyGoldRewardRepository {

    private final DailyGoldRewardMapper dailyGoldRewardMapper;

    public DailyGoldRewardRepositoryImpl(DailyGoldRewardMapper dailyGoldRewardMapper) {
        this.dailyGoldRewardMapper = dailyGoldRewardMapper;
    }

    @Override
    public boolean hasClaimed(User user, RewardDate rewardDate) {
        int count = dailyGoldRewardMapper.countByUserAndDate(user.getId(), rewardDate.getValue());
        return count > 0;
    }

    @Override
    public void claim(DailyGoldReward reward) {
        DailyGoldRewardDbDto dbDto = DailyGoldRewardDbDto.from(reward);
        dailyGoldRewardMapper.insert(dbDto);
    }
}