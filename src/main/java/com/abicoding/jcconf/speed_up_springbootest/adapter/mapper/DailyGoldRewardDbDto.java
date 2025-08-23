package com.abicoding.jcconf.speed_up_springbootest.adapter.mapper;

import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
import lombok.Data;

@Data
public class DailyGoldRewardDbDto {
    private Long id;
    private Long userId;
    private Integer rewardDate;
    private Long amount;
    private Long createdAt;

    public static DailyGoldRewardDbDto from(DailyGoldReward reward) {
        DailyGoldRewardDbDto dbDto = new DailyGoldRewardDbDto();
        dbDto.setId(reward.getId());
        dbDto.setUserId(reward.getUserId());
        dbDto.setRewardDate(reward.getRewardDate().getValue());
        dbDto.setAmount(reward.getAmount());
        dbDto.setCreatedAt(reward.getCreatedAt().toEpochMilli());
        return dbDto;
    }
}