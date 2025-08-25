package com.abicoding.jcconf.speed_up_springbootest.adapter.repository;

import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.WalletDbDto;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.WalletMapper;
import com.abicoding.jcconf.speed_up_springbootest.entity.Wallet;
import com.abicoding.jcconf.speed_up_springbootest.service.WalletRepository;
import org.springframework.stereotype.Repository;

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
    public void save(Wallet wallet) {
        WalletDbDto walletDbDto = WalletDbDto.from(wallet);
        int affectedRows = walletMapper.update(walletDbDto);
        if (affectedRows == 0) {
            throw new OptimisticLockException("userId=%s, version=%s".formatted(
                    wallet.getUserId(), wallet.getVersion()));
        }
    }
}