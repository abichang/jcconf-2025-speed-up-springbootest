package com.abicoding.jcconf.speed_up_springbootest.adapter.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MybatisTest
class DailyGoldRewardMapperTest {

    @Autowired
    private DailyGoldRewardMapper dailyGoldRewardMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    void insert_and_countByUserAndDate() {
        UserDbDto user = given_user();

        DailyGoldRewardDbDto reward = given_DailyGoldRewardDbDto(user);
        dailyGoldRewardMapper.insert(reward);

        int count = dailyGoldRewardMapper.countByUserAndDate(user.getId(), reward.getRewardDate());
        assertThat(count).isEqualTo(1);
    }

    private UserDbDto given_user() {
        long now = Instant.now().toEpochMilli();
        UserDbDto user = new UserDbDto();
        user.setUsername("testuser");
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        userMapper.insert(user);
        return user;
    }

    private DailyGoldRewardDbDto given_DailyGoldRewardDbDto(UserDbDto user) {
        DailyGoldRewardDbDto reward = new DailyGoldRewardDbDto();
        reward.setUserId(user.getId());
        reward.setRewardDate(20240115);
        reward.setAmount(10L);
        reward.setCreatedAt(Instant.now().toEpochMilli());
        return reward;
    }

    @Test
    void countByUserAndDate_not_exists() {
        UserDbDto user = given_user();

        Integer nonExistentDate = 20240116;
        int count = dailyGoldRewardMapper.countByUserAndDate(user.getId(), nonExistentDate);

        assertThat(count).isZero();
    }

    @Test
    void duplicate_date() {
        UserDbDto user = given_user();

        DailyGoldRewardDbDto reward = given_DailyGoldRewardDbDto(user);
        dailyGoldRewardMapper.insert(reward);

        assertThatThrownBy(() -> dailyGoldRewardMapper.insert(reward))
                .isInstanceOf(DuplicateKeyException.class);
    }
}