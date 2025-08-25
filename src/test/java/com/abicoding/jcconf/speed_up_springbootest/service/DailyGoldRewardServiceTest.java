package com.abicoding.jcconf.speed_up_springbootest.service;

import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
import com.abicoding.jcconf.speed_up_springbootest.entity.RewardDate;
import com.abicoding.jcconf.speed_up_springbootest.entity.User;
import com.abicoding.jcconf.speed_up_springbootest.entity.Wallet;
import com.abicoding.jcconf.speed_up_springbootest.util.TimeUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
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
        given_wallet(user.getId(), 500L, now);
        RewardDate rewardDate = RewardDate.restore(20240115);

        given_not_claim_yet(user, rewardDate);

        dailyGoldRewardService.claim(user.getId());

        then_wallet_should_updated(510L, now);

        DailyGoldReward actualDailyGoldReward = captureActualDailyGoldReward();
        assertThat(actualDailyGoldReward.getUserId()).isEqualTo(user.getId());
        assertThat(actualDailyGoldReward.getRewardDate()).isEqualTo(rewardDate);
        assertThat(actualDailyGoldReward.getAmount()).isEqualTo(10L);
        assertThat(actualDailyGoldReward.getCreatedAt()).isEqualTo(now);
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

    private void given_wallet(Long userId, long gold, Instant updatedAt) {
        Wallet wallet = Wallet.restore(userId, gold, 1L, Instant.ofEpochMilli(0L), updatedAt);
        doReturn(wallet).when(walletRepository).getByUserId(userId);
    }

    private void given_not_claim_yet(User user, RewardDate rewardDate) {
        doReturn(false)
                .when(dailyGoldRewardRepository)
                .hasClaimed(user, rewardDate);
    }

    private void then_wallet_should_updated(long updatedGold, Instant updatedAt) {
        ArgumentCaptor<Wallet> walletCaptor = ArgumentCaptor.forClass(Wallet.class);
        verify(walletRepository).save(walletCaptor.capture());
        Wallet updatedWallet = walletCaptor.getValue();
        assertThat(updatedWallet.getGold()).isEqualTo(updatedGold);
        assertThat(updatedWallet.getUpdatedAt()).isEqualTo(updatedAt);
    }

    private DailyGoldReward captureActualDailyGoldReward() {
        ArgumentCaptor<DailyGoldReward> rewardCaptor = ArgumentCaptor.forClass(DailyGoldReward.class);
        verify(dailyGoldRewardRepository).claim(rewardCaptor.capture());
        return rewardCaptor.getValue();
    }

    @Test
    void duplicate_claim() {
        given_now("2024-01-15T10:00:00Z");

        User user = given_user();
        given_wallet(user.getId(), 500L, Instant.now());
        RewardDate rewardDate = RewardDate.restore(20240115);

        given_already_claimed(user, rewardDate);

        DailyGoldenClaimedException actualException = assertThrows(
                DailyGoldenClaimedException.class,
                () -> dailyGoldRewardService.claim(user.getId())
        );

        assertThat(actualException.getMessage()).isEqualTo("userId=" + user.getId());
        verify(walletRepository, never()).save(any(Wallet.class));
        verify(dailyGoldRewardRepository, never()).claim(any(DailyGoldReward.class));
    }

    private void given_already_claimed(User user, RewardDate rewardDate) {
        doReturn(true)
                .when(dailyGoldRewardRepository)
                .hasClaimed(user, rewardDate);
    }

    @Test
    void handle_user_not_found() {
        Long userId = 404L;

        given_now("2024-01-15T10:00:00Z");
        given_user_not_found(userId);

        UserNotFoundException actualException = assertThrows(
                UserNotFoundException.class,
                () -> dailyGoldRewardService.claim(userId)
        );

        assertThat(actualException.getMessage()).isEqualTo("userId=" + userId);
        verify(walletRepository, never()).getByUserId(anyLong());
        verify(walletRepository, never()).save(any(Wallet.class));
        verify(dailyGoldRewardRepository, never()).claim(any(DailyGoldReward.class));
    }

    private void given_user_not_found(Long userId) {
        doThrow(new UserNotFoundException("userId=" + userId))
                .when(userRepository).getById(userId);
    }

    @Test
    void handle_optimistic_lock_conflict() {
        Instant now = given_now("2024-01-15T10:00:00Z");

        User user = given_user();
        given_wallet(user.getId(), 500L, now);
        RewardDate rewardDate = RewardDate.restore(20240115);
        given_not_claim_yet(user, rewardDate);

        doThrow(new OptimisticLockException("userId=1, version=1"))
                .when(walletRepository).save(any(Wallet.class));

        OptimisticLockException actualException = assertThrows(
                OptimisticLockException.class,
                () -> dailyGoldRewardService.claim(user.getId())
        );

        assertThat(actualException.getMessage()).isEqualTo("userId=1, version=1");
        verify(dailyGoldRewardRepository, never()).claim(any(DailyGoldReward.class));
    }
}