package com.abicoding.jcconf.speed_up_springbootest.service;

import com.abicoding.jcconf.speed_up_springbootest.entity.Wallet;

public interface WalletRepository {
    Wallet getByUserId(Long userId);

    void save(Wallet wallet);
}