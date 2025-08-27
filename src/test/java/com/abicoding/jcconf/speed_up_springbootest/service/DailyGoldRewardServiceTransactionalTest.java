package com.abicoding.jcconf.speed_up_springbootest.service;

import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.UserDbDto;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.UserMapper;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.WalletDbDto;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.WalletMapper;
import com.abicoding.jcconf.speed_up_springbootest.common.SystemTestBase;
import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
import com.abicoding.jcconf.speed_up_springbootest.entity.RewardDate;
import com.abicoding.jcconf.speed_up_springbootest.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

class DailyGoldRewardServiceTransactionalTest extends SystemTestBase {

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    void claim_fail_wallet_rollback() {

        UserDbDto user = given_user();

        WalletDbDto originalWallet = given_wallet(user, 500L);

        given_not_claim_yet();

        given_error_when_claiming();

        assertThrows(
                RuntimeException.class,
                () -> dailyGoldRewardService.claim(user.getId()));

        WalletDbDto rollbackWallet = walletMapper.selectByUserId(user.getId());
        assertThat(rollbackWallet).isEqualTo(originalWallet);
    }

    private UserDbDto given_user() {
        UserDbDto user = new UserDbDto();
        user.setId(1L);
        user.setUsername("test-user");
        user.setCreatedAt(0L);
        user.setUpdatedAt(0L);
        userMapper.insert(user);
        return user;
    }

    private WalletDbDto given_wallet(UserDbDto user, Long initialGold) {
        WalletDbDto walletDbDto = new WalletDbDto();
        walletDbDto.setUserId(user.getId());
        walletDbDto.setGold(initialGold);
        walletDbDto.setVersion(1L);
        walletDbDto.setCreatedAt(0L);
        walletDbDto.setUpdatedAt(0L);
        walletMapper.insert(walletDbDto);
        return walletDbDto;
    }

    private void given_not_claim_yet() {
        doReturn(false)
                .when(dailyGoldRewardRepository)
                .hasClaimed(any(User.class), any(RewardDate.class));
    }

    private void given_error_when_claiming() {
        doThrow(new RuntimeException("Database error"))
                .when(dailyGoldRewardRepository).claim(any(DailyGoldReward.class));
    }
}