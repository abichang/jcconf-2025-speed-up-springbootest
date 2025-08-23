package com.abicoding.jcconf.speed_up_springbootest.adapter.repository;

import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.DailyGoldRewardDbDto;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.DailyGoldRewardMapper;
import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldRewardRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Repository
public class DailyGoldRewardRepositoryImpl implements DailyGoldRewardRepository {

    private final DailyGoldRewardMapper dailyGoldRewardMapper;

    public DailyGoldRewardRepositoryImpl(DailyGoldRewardMapper dailyGoldRewardMapper) {
        this.dailyGoldRewardMapper = dailyGoldRewardMapper;
    }

    @Override
    public boolean hasClaimed(Long userId, Instant date) {
        Integer rewardDate = Integer.valueOf(date.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        int count = dailyGoldRewardMapper.countByUserAndDate(userId, rewardDate);
        return count > 0;
    }

    @Override
    public void claim(DailyGoldReward reward) {
        DailyGoldRewardDbDto dbDto = DailyGoldRewardDbDto.from(reward);
        dailyGoldRewardMapper.insert(dbDto);
    }
}