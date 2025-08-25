package com.abicoding.jcconf.speed_up_springbootest.adapter.repository;

import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.WalletDbDto;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.WalletMapper;
import com.abicoding.jcconf.speed_up_springbootest.entity.Wallet;
import com.abicoding.jcconf.speed_up_springbootest.service.WalletRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class WalletRepositoryImplTest {

    private final WalletMapper walletMapper = mock(WalletMapper.class);
    private final WalletRepository walletRepository = new WalletRepositoryImpl(walletMapper);

    @Test
    void getByUserId_all_ok() {
        WalletDbDto walletDbDto = given_wallet_in_db();

        Wallet actualWallet = walletRepository.getByUserId(walletDbDto.getUserId());

        assertThat(actualWallet.getUserId()).isEqualTo(walletDbDto.getUserId());
        assertThat(actualWallet.getGold()).isEqualTo(walletDbDto.getGold());
        assertThat(actualWallet.getCreatedAt()).isEqualTo(Instant.ofEpochMilli(walletDbDto.getCreatedAt()));
        assertThat(actualWallet.getUpdatedAt()).isEqualTo(Instant.ofEpochMilli(walletDbDto.getUpdatedAt()));
    }

    private WalletDbDto given_wallet_in_db() {
        WalletDbDto wallet = new WalletDbDto();
        wallet.setUserId(1L);
        wallet.setGold(500L);
        wallet.setCreatedAt(1640995200000L);
        wallet.setUpdatedAt(1640995200000L);
        Mockito.doReturn(wallet).when(walletMapper).selectByUserId(wallet.getUserId());
        return wallet;
    }

    @Test
    void getByUserId_wallet_not_existed() {
        Long userWithoutWallet = 404L;

        Mockito.doReturn(null)
                .when(walletMapper)
                .selectByUserId(userWithoutWallet);

        assertThatThrownBy(() -> walletRepository.getByUserId(userWithoutWallet))
                .isInstanceOf(WalletNotFoundException.class)
                .hasMessage("userId=" + userWithoutWallet);
    }

    @Test
    void save_success() {
        given_save_wallet_success();

        Wallet wallet = Wallet.restore(1L, 150L, 1L, Instant.now(), Instant.now());
        walletRepository.save(wallet);

        verify(walletMapper, times(1)).update(any(WalletDbDto.class));
    }

    private void given_save_wallet_success() {
        doReturn(1).when(walletMapper).update(any(WalletDbDto.class));
    }

    @Test
    void save_optimistic_lock_conflict() {
        given_save_wallet_failed();

        Wallet wallet = Wallet.restore(1L, 150L, 1L, Instant.now(), Instant.now());
        assertThatThrownBy(() -> walletRepository.save(wallet))
                .isInstanceOf(OptimisticLockException.class)
                .hasMessage("userId=1, version=1");
    }

    private void given_save_wallet_failed() {
        doReturn(0).when(walletMapper).update(any(WalletDbDto.class));
    }
}