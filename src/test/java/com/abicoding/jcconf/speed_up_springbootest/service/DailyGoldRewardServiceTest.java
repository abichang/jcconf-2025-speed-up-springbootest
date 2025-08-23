package com.abicoding.jcconf.speed_up_springbootest.service;

import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
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
    private final DailyGoldRewardService dailyGoldRewardService = new DailyGoldRewardService(
            userRepository,
            walletRepository,
            dailyGoldRewardRepository
    );


    @Test
    void claim_all_ok() {
        Long userId = 1L;
        Instant now = Instant.now();

        when(dailyGoldRewardRepository.hasClaimed(eq(userId), any(Instant.class))).thenReturn(false);

        dailyGoldRewardService.claim(userId);

        verify(walletRepository).addGold(eq(userId), eq(10L), any(Instant.class));

        ArgumentCaptor<DailyGoldReward> rewardCaptor = ArgumentCaptor.forClass(DailyGoldReward.class);
        verify(dailyGoldRewardRepository).claim(rewardCaptor.capture());

        DailyGoldReward capturedReward = rewardCaptor.getValue();
        assertEquals(userId, capturedReward.getUserId());
        assertEquals(10L, capturedReward.getAmount());
        assertNotNull(capturedReward.getCreatedAt());
        assertEquals(TimeUtils.toYYYYMMDD(capturedReward.getCreatedAt()), capturedReward.getRewardDate());
    }

    @Test
    void duplicate_claim() {
        Long userId = 1L;

        when(dailyGoldRewardRepository.hasClaimed(eq(userId), any(Instant.class))).thenReturn(true);

        DailyGoldenClaimedException exception = assertThrows(
                DailyGoldenClaimedException.class,
                () -> dailyGoldRewardService.claim(userId)
        );

        assertEquals("userId=1", exception.getMessage());

        verify(walletRepository, never()).addGold(anyLong(), anyLong(), any(Instant.class));
        verify(dailyGoldRewardRepository, never()).claim(any(DailyGoldReward.class));
    }
}