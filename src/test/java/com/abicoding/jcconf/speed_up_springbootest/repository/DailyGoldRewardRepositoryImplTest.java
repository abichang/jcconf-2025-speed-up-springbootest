package com.abicoding.jcconf.speed_up_springbootest.repository;

import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.DailyGoldRewardDbDto;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.DailyGoldRewardMapper;
import com.abicoding.jcconf.speed_up_springbootest.adapter.repository.DailyGoldRewardRepositoryImpl;
import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldRewardRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DailyGoldRewardRepositoryImplTest {

    private final DailyGoldRewardMapper dailyGoldRewardMapper = mock(DailyGoldRewardMapper.class);
    private final DailyGoldRewardRepository dailyGoldRewardRepository = new DailyGoldRewardRepositoryImpl(dailyGoldRewardMapper);
    private final Long userId = 1L;

    @Test
    void claimed() {
        Instant date = Instant.parse("2024-01-15T10:00:00Z");

        given_claimed(userId, 20240115);

        boolean hasClaimed = dailyGoldRewardRepository.hasClaimed(userId, date);

        assertThat(hasClaimed).isTrue();
    }

    private void given_claimed(Long userId, int rewardDate) {
        Mockito.doReturn(1).when(dailyGoldRewardMapper).countByUserAndDate(userId, rewardDate);
    }

    @Test
    void not_claimed() {
        Instant date = Instant.parse("2024-01-15T10:00:00Z");

        given_not_claimed(userId, 20240115);

        boolean hasClaimed = dailyGoldRewardRepository.hasClaimed(userId, date);

        assertThat(hasClaimed).isFalse();
    }

    private void given_not_claimed(Long userId, int rewardDate) {
        Mockito.doReturn(0).when(dailyGoldRewardMapper).countByUserAndDate(userId, rewardDate);
    }

    @Test
    void claim_all_ok() {
        DailyGoldReward reward = daily_gold_reward();

        dailyGoldRewardRepository.claim(reward);

        then_should_save_into_db(reward);
    }

    private DailyGoldReward daily_gold_reward() {
        DailyGoldReward reward = new DailyGoldReward();
        reward.setId(1L);
        reward.setUserId(1L);
        reward.setRewardDate(20240115);
        reward.setAmount(10L);
        reward.setCreatedAt(Instant.now());
        return reward;
    }

    private void then_should_save_into_db(DailyGoldReward reward) {
        ArgumentCaptor<DailyGoldRewardDbDto> dailyGoldRewardDbDtoCaptor = ArgumentCaptor.forClass(DailyGoldRewardDbDto.class);
        verify(dailyGoldRewardMapper, times(1)).insert(dailyGoldRewardDbDtoCaptor.capture());

        DailyGoldRewardDbDto dailyGoldRewardDbDto = dailyGoldRewardDbDtoCaptor.getValue();
        assertThat(dailyGoldRewardDbDto.getId()).isEqualTo(reward.getId());
        assertThat(dailyGoldRewardDbDto.getUserId()).isEqualTo(reward.getUserId());
        assertThat(dailyGoldRewardDbDto.getRewardDate()).isEqualTo(reward.getRewardDate());
        assertThat(dailyGoldRewardDbDto.getAmount()).isEqualTo(reward.getAmount());
        assertThat(dailyGoldRewardDbDto.getCreatedAt()).isEqualTo(reward.getCreatedAt().toEpochMilli());
    }

    @Test
    void handle_duplicate_reward() {
        Mockito.doThrow(DuplicateKeyException.class)
                .when(dailyGoldRewardMapper)
                .insert(any(DailyGoldRewardDbDto.class));

        assertThatThrownBy(() -> dailyGoldRewardRepository.claim(daily_gold_reward()))
                .isInstanceOf(DuplicateKeyException.class);
    }
}