package com.abicoding.jcconf.speed_up_springbootest.service;

import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
import com.abicoding.jcconf.speed_up_springbootest.entity.RewardDate;
import com.abicoding.jcconf.speed_up_springbootest.entity.User;
import com.abicoding.jcconf.speed_up_springbootest.util.TimeUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DailyGoldRewardServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final WalletRepository walletRepository = Mockito.mock(WalletRepository.class);
    private final DailyGoldRewardRepository dailyGoldRewardRepository = Mockito.mock(DailyGoldRewardRepository.class);
    private final TimeUtils timeUtils = Mockito.mock(TimeUtils.class);
    private final DailyGoldRewardService dailyGoldRewardService = new DailyGoldRewardService(
            userRepository,
            walletRepository,
            dailyGoldRewardRepository,
            timeUtils
    );

    @Test
    void claim_all_ok() {
        Instant now = given_now("2024-01-15T10:00:00Z");

        User user = given_user();
        RewardDate rewardDate = RewardDate.restore(20240115);

        given_not_claim_yet(user, rewardDate);

        dailyGoldRewardService.claim(user.getId());

        verify(walletRepository).addGold(user.getId(), 10L, now);

        ArgumentCaptor<DailyGoldReward> rewardCaptor = ArgumentCaptor.forClass(DailyGoldReward.class);
        verify(dailyGoldRewardRepository).claim(rewardCaptor.capture());

        DailyGoldReward capturedReward = rewardCaptor.getValue();
        assertEquals(user.getId(), capturedReward.getUserId());
        assertEquals(10L, capturedReward.getAmount());
        assertEquals(now, capturedReward.getCreatedAt());
        assertEquals(RewardDate.restore(20240115), capturedReward.getRewardDate());
    }

    private Instant given_now(String timeText) {
        Instant now = Instant.parse(timeText);
        doReturn(now).when(timeUtils).now();
        return now;
    }

    private User given_user() {
        User user = new User();
        user.setId(1L);
        doReturn(user).when(userRepository).getById(user.getId());
        return user;
    }

    private void given_not_claim_yet(User user, RewardDate rewardDate) {
        doReturn(false)
                .when(dailyGoldRewardRepository)
                .hasClaimed(user, rewardDate);
    }

    @Test
    void duplicate_claim() {
        given_now("2024-01-15T10:00:00Z");

        User user = given_user();
        RewardDate rewardDate = RewardDate.restore(20240115);

        given_already_claimed(user, rewardDate);

        DailyGoldenClaimedException exception = assertThrows(
                DailyGoldenClaimedException.class,
                () -> dailyGoldRewardService.claim(user.getId())
        );

        assertEquals("userId=1", exception.getMessage());

        verify(walletRepository, never()).addGold(anyLong(), anyLong(), any(Instant.class));
        verify(dailyGoldRewardRepository, never()).claim(any(DailyGoldReward.class));
    }

    private void given_already_claimed(User user, RewardDate rewardDate) {
        doReturn(true)
                .when(dailyGoldRewardRepository)
                .hasClaimed(user, rewardDate);
    }
}