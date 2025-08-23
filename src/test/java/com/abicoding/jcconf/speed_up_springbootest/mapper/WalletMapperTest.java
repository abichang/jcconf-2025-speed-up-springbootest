package com.abicoding.jcconf.speed_up_springbootest.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
class WalletMapperTest {

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    void insert_and_selectByUserId() {
        UserDbDto user = given_user();

        WalletDbDto insertWallet = given_WalletDbDto(user);
        walletMapper.insert(insertWallet);

        WalletDbDto foundWallet = walletMapper.selectByUserId(user.getId());
        assertThat(foundWallet)
                .isNotNull()
                .isEqualTo(insertWallet);
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

    private WalletDbDto given_WalletDbDto(UserDbDto user) {
        WalletDbDto wallet = new WalletDbDto();
        wallet.setUserId(user.getId());
        wallet.setGold(500L);
        wallet.setCreatedAt(user.getCreatedAt());
        wallet.setUpdatedAt(user.getUpdatedAt());
        return wallet;
    }

    @Test
    void addGold_all_ok() {
        UserDbDto user = given_user();
        WalletDbDto originalWallet = given_wallet(user);

        long updatedAt = Instant.now().toEpochMilli();
        walletMapper.addGold(user.getId(), 10L, updatedAt);

        WalletDbDto addedWallet = walletMapper.selectByUserId(user.getId());
        assertThat(addedWallet.getGold()).isEqualTo(originalWallet.getGold() + 10L);
        assertThat(addedWallet.getUpdatedAt()).isEqualTo(updatedAt);
    }

    private WalletDbDto given_wallet(UserDbDto user) {
        WalletDbDto wallet = given_WalletDbDto(user);
        walletMapper.insert(wallet);
        return wallet;
    }

    @Test
    void selectByUserId_wallet_not_exists() {
        Long nonExistentUserId = 999L;

        WalletDbDto actualWallet = walletMapper.selectByUserId(nonExistentUserId);

        assertThat(actualWallet).isNull();
    }
}