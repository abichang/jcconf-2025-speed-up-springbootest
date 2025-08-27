package com.abicoding.jcconf.speed_up_springbootest.adapter.controller;

import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.*;
import com.abicoding.jcconf.speed_up_springbootest.adapter.repository.DailyGoldRewardRepositoryImpl;
import com.abicoding.jcconf.speed_up_springbootest.adapter.repository.UserRepositoryImpl;
import com.abicoding.jcconf.speed_up_springbootest.adapter.repository.WalletRepositoryImpl;
import com.abicoding.jcconf.speed_up_springbootest.common.SystemTestBase;
import com.abicoding.jcconf.speed_up_springbootest.entity.Wallet;
import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldRewardService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class DailyGoldRewardControllerIntegrationTest extends SystemTestBase {

    @Autowired
    private WalletRepositoryImpl walletRepository;
    private DailyGoldRewardController dailyGoldRewardController;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private DailyGoldRewardMapper dailyGoldRewardMapper;

    @BeforeEach
    void setUp() {
        dailyGoldRewardController = new DailyGoldRewardController(new DailyGoldRewardService(
                new UserRepositoryImpl(userMapper),
                walletRepository,
                new DailyGoldRewardRepositoryImpl(dailyGoldRewardMapper),
                timeUtils));
    }

    @SneakyThrows
    @Test
    void claim_all_ok() {
        given_now("2024-01-15T10:00:00Z");

        Long userId = given_user("user1");

        given_wallet(userId, 500L, 1L);

        mockMvc.perform(post("/user/{user_id}/daily-golden", userId))
                .andExpect(status().isOk());

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

    private Long given_user(String username) {
        UserDbDto user = new UserDbDto();
        user.setUsername(username);
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

    @SneakyThrows
    @Test
    void claim_duplicate_same_day() {
        given_now("2024-01-15T10:00:00Z");

        Long userId = given_user("user1");
        given_wallet(userId, 500L, 1L);

        mockMvc.perform(post("/user/{user_id}/daily-golden", userId))
                .andExpect(status().isOk());

        mockMvc.perform(post("/user/{user_id}/daily-golden", userId))
                .andExpect(status().isConflict());

        Wallet updatedWallet = walletRepository.getByUserId(userId);
        assertThat(updatedWallet.getGold()).isEqualTo(510L);
        assertThat(updatedWallet.getVersion()).isEqualTo(2L);

        assertThat(dailyGoldRewardMapper.countByUserAndDate(userId, 20240115))
                .isEqualTo(1);
    }

    @Test
    void user_not_found() {
        given_now("2024-01-15T10:00:00Z");

        Long nonExistentUserId = 999L;

        ResponseEntity<Void> response = dailyGoldRewardController.claimDailyGold(nonExistentUserId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        assertThat(dailyGoldRewardMapper.countByUserAndDate(nonExistentUserId, 20240115))
                .isEqualTo(0);
    }

    @Test
    void claim_utc_midnight_reset() {
        Long userId = given_user("user1");
        given_wallet(userId, 500L, 1L);

        given_now("2024-01-15T10:00:00Z");
        ResponseEntity<Void> firstResponse = dailyGoldRewardController.claimDailyGold(userId);
        assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Wallet walletAfterFirstClaim = walletRepository.getByUserId(userId);
        assertThat(walletAfterFirstClaim.getGold()).isEqualTo(510L);
        assertThat(walletAfterFirstClaim.getVersion()).isEqualTo(2L);

        assertThat(dailyGoldRewardMapper.countByUserAndDate(userId, 20240115))
                .isEqualTo(1);

        given_now("2024-01-16T02:00:00Z");
        ResponseEntity<Void> secondResponse = dailyGoldRewardController.claimDailyGold(userId);
        assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Wallet walletAfterSecondClaim = walletRepository.getByUserId(userId);
        assertThat(walletAfterSecondClaim.getGold()).isEqualTo(520L);
        assertThat(walletAfterSecondClaim.getVersion()).isEqualTo(3L);

        assertThat(dailyGoldRewardMapper.countByUserAndDate(userId, 20240116))
                .isEqualTo(1);
    }

    @Test
    void claim_multiple_users_same_day() {
        given_now("2024-01-15T10:00:00Z");

        Long user1Id = given_user("user1");
        given_wallet(user1Id, 100L, 1L);

        ResponseEntity<Void> response1 = dailyGoldRewardController.claimDailyGold(user1Id);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);

        Wallet wallet1 = walletRepository.getByUserId(user1Id);
        assertThat(wallet1.getGold()).isEqualTo(110L);
        assertThat(wallet1.getVersion()).isEqualTo(2L);

        assertThat(dailyGoldRewardMapper.countByUserAndDate(user1Id, 20240115))
                .isEqualTo(1);


        Long user2Id = given_user("user2");
        given_wallet(user2Id, 200L, 1L);

        ResponseEntity<Void> response2 = dailyGoldRewardController.claimDailyGold(user2Id);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);

        Wallet wallet2 = walletRepository.getByUserId(user2Id);
        assertThat(wallet2.getGold()).isEqualTo(210L);
        assertThat(wallet2.getVersion()).isEqualTo(2L);

        assertThat(dailyGoldRewardMapper.countByUserAndDate(user2Id, 20240115))
                .isEqualTo(1);
    }
}