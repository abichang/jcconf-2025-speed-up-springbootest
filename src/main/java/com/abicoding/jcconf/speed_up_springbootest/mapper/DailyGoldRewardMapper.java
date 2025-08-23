package com.abicoding.jcconf.speed_up_springbootest.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DailyGoldRewardMapper {

    @Select("""
            SELECT COUNT(*) 
            FROM daily_gold_reward 
            WHERE user_id = #{userId} AND reward_date = #{rewardDate}
            """)
    int countByUserAndDate(Long userId, Integer rewardDate);

    @Insert("""
            INSERT INTO daily_gold_reward (user_id, reward_date, amount, created_at) 
            VALUES (#{userId}, #{rewardDate}, #{amount}, #{createdAt})
            """)
    void insert(DailyGoldRewardDbDto reward);
}