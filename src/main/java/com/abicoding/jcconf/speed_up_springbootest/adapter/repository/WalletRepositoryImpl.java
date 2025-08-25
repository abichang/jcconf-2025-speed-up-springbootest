package com.abicoding.jcconf.speed_up_springbootest.adapter.repository;

import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.WalletDbDto;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.WalletMapper;
import com.abicoding.jcconf.speed_up_springbootest.entity.Wallet;
import com.abicoding.jcconf.speed_up_springbootest.service.WalletNotFoundException;
import com.abicoding.jcconf.speed_up_springbootest.service.WalletRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class WalletRepositoryImpl implements WalletRepository {

    private final WalletMapper walletMapper;

    public WalletRepositoryImpl(WalletMapper walletMapper) {
        this.walletMapper = walletMapper;
    }

    @Override
    public Wallet getByUserId(Long userId) {
        WalletDbDto walletDbDto = walletMapper.selectByUserId(userId);
        if (walletDbDto == null) {
            throw new WalletNotFoundException("userId=%s".formatted(userId));
        }
        return walletDbDto.toEntity();
    }

    @Override
    public void addGold(Long userId, Long amount, Instant updatedAt) {
        walletMapper.addGold(userId, amount, updatedAt.toEpochMilli());
    }

    @Override
    public void save(Wallet wallet) {
        // TODO: 實作樂觀鎖更新邏輯 (Task 48)
        throw new UnsupportedOperationException("save method not implemented yet");
    }
}