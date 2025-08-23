package com.abicoding.jcconf.speed_up_springbootest.service;

import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
import com.abicoding.jcconf.speed_up_springbootest.entity.RewardDate;
import com.abicoding.jcconf.speed_up_springbootest.util.TimeUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
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
        Long userId = 1L;
        Instant now = Instant.parse("2024-01-15T10:00:00Z");

        when(timeUtils.now()).thenReturn(now);
        when(dailyGoldRewardRepository.hasClaimed(userId, now)).thenReturn(false);

        dailyGoldRewardService.claim(userId);

        verify(walletRepository).addGold(userId, 10L, now);

        ArgumentCaptor<DailyGoldReward> rewardCaptor = ArgumentCaptor.forClass(DailyGoldReward.class);
        verify(dailyGoldRewardRepository).claim(rewardCaptor.capture());

        DailyGoldReward capturedReward = rewardCaptor.getValue();
        assertEquals(userId, capturedReward.getUserId());
        assertEquals(10L, capturedReward.getAmount());
        assertEquals(now, capturedReward.getCreatedAt());
        assertEquals(RewardDate.create(now), capturedReward.getRewardDate());
    }

    @Test
    void duplicate_claim() {
        Long userId = 1L;
        Instant now = Instant.parse("2024-01-15T10:00:00Z");

        when(timeUtils.now()).thenReturn(now);
        when(dailyGoldRewardRepository.hasClaimed(userId, now)).thenReturn(true);

        DailyGoldenClaimedException exception = assertThrows(
                DailyGoldenClaimedException.class,
                () -> dailyGoldRewardService.claim(userId)
        );

        assertEquals("userId=1", exception.getMessage());

        verify(walletRepository, never()).addGold(anyLong(), anyLong(), any(Instant.class));
        verify(dailyGoldRewardRepository, never()).claim(any(DailyGoldReward.class));
    }
}