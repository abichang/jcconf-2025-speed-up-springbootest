package com.abicoding.jcconf.speed_up_springbootest.adapter.controller;

import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.*;
import com.abicoding.jcconf.speed_up_springbootest.adapter.repository.DailyGoldRewardRepositoryImpl;
import com.abicoding.jcconf.speed_up_springbootest.adapter.repository.UserRepositoryImpl;
import com.abicoding.jcconf.speed_up_springbootest.adapter.repository.WalletRepositoryImpl;
import com.abicoding.jcconf.speed_up_springbootest.common.InjectMapper;
import com.abicoding.jcconf.speed_up_springbootest.common.SystemDbTest;
import com.abicoding.jcconf.speed_up_springbootest.entity.Wallet;
import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldRewardService;
import com.abicoding.jcconf.speed_up_springbootest.util.TimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@SystemDbTest
class DailyGoldRewardControllerIntegrationTest {

    private final TimeUtils timeUtils = Mockito.mock(TimeUtils.class);
    private WalletRepositoryImpl walletRepository;
    private DailyGoldRewardController dailyGoldRewardController;

    @InjectMapper
    private UserMapper userMapper;

    @InjectMapper
    private WalletMapper walletMapper;

    @InjectMapper
    private DailyGoldRewardMapper dailyGoldRewardMapper;

    @BeforeEach
    void setUp() {
        walletRepository = new WalletRepositoryImpl(walletMapper);
        dailyGoldRewardController = new DailyGoldRewardController(new DailyGoldRewardService(
                new UserRepositoryImpl(userMapper),
                walletRepository,
                new DailyGoldRewardRepositoryImpl(dailyGoldRewardMapper),
                timeUtils));
    }

    @Test
    void claim_all_ok() {
        given_now("2024-01-15T10:00:00Z");

        Long userId = given_user();

        given_wallet(userId, 500L, 1L);

        ResponseEntity<Void> response = dailyGoldRewardController.claimDailyGold(userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Wallet updatedWallet = walletRepository.getByUserId(userId);
        assertThat(updatedWallet.getGold()).isEqualTo(510L);
        assertThat(updatedWallet.getVersion()).isEqualTo(2L);

        assertThat(dailyGoldRewardMapper.countByUserAndDate(userId, 20240115))
                .isEqualTo(1);
    }

    private void given_now(String timeText) {
        Instant now = Instant.parse(timeText);
        doReturn(now).when(timeUtils).now();
    }

    private Long given_user() {
        UserDbDto user = new UserDbDto();
        user.setUsername("testuser");
        user.setCreatedAt(0L);
        user.setUpdatedAt(0L);
        userMapper.insert(user);
        return user.getId();
    }

    private void given_wallet(Long userId, long initialGold, long initialVersion) {
        WalletDbDto wallet = new WalletDbDto();
        wallet.setUserId(userId);
        wallet.setGold(initialGold);
        wallet.setVersion(initialVersion);
        wallet.setCreatedAt(0L);
        wallet.setUpdatedAt(0L);
        walletMapper.insert(wallet);
    }

    @Test
    void claim_duplicate_same_day() {
        given_now("2024-01-15T10:00:00Z");

        Long userId = given_user();
        given_wallet(userId, 500L, 1L);

        ResponseEntity<Void> firstResponse = dailyGoldRewardController.claimDailyGold(userId);
        assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Void> secondResponse = dailyGoldRewardController.claimDailyGold(userId);
        assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        Wallet updatedWallet = walletRepository.getByUserId(userId);
        assertThat(updatedWallet.getGold()).isEqualTo(510L);
        assertThat(updatedWallet.getVersion()).isEqualTo(2L);

        assertThat(dailyGoldRewardMapper.countByUserAndDate(userId, 20240115))
                .isEqualTo(1);
    }

    @Test
    void claim_user_not_found() {
        given_now("2024-01-15T10:00:00Z");

        Long nonExistentUserId = 999L;

        ResponseEntity<Void> response = dailyGoldRewardController.claimDailyGold(nonExistentUserId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(dailyGoldRewardMapper.countByUserAndDate(nonExistentUserId, 20240115))
                .isEqualTo(0);
    }
}