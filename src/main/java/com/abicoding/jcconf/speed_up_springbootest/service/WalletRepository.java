package com.abicoding.jcconf.speed_up_springbootest.service;

import com.abicoding.jcconf.speed_up_springbootest.entity.Wallet;

import java.time.Instant;

public interface WalletRepository {
    Wallet getByUserId(Long userId);
    void addGold(Long userId, Long amount, Instant updatedAt);
    void save(Wallet wallet);
}