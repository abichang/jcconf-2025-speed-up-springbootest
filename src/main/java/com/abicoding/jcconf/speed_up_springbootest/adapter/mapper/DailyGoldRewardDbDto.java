package com.abicoding.jcconf.speed_up_springbootest.adapter.mapper;

import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class DailyGoldRewardDbDto {
    private Long id;
    private Long userId;
    private Integer rewardDate;
    private Long amount;
    private Long createdAt;

    public DailyGoldReward toEntity() {
        DailyGoldReward reward = new DailyGoldReward();
        reward.setId(getId());
        reward.setUserId(getUserId());
        reward.setRewardDate(getRewardDate());
        reward.setAmount(getAmount());
        reward.setCreatedAt(Instant.ofEpochMilli(getCreatedAt()));
        return reward;
    }
}