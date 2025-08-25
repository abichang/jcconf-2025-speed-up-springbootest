package com.abicoding.jcconf.speed_up_springbootest.adapter.mapper;

import com.abicoding.jcconf.speed_up_springbootest.entity.Wallet;
import lombok.Data;

import java.time.Instant;

@Data
public class WalletDbDto {
    private Long userId;
    private Long gold;
    private Long version;
    private Long createdAt;
    private Long updatedAt;

    public Wallet toEntity() {
        Wallet wallet = new Wallet();
        wallet.setUserId(getUserId());
        wallet.setGold(getGold());
        wallet.setVersion(getVersion());
        wallet.setCreatedAt(Instant.ofEpochMilli(getCreatedAt()));
        wallet.setUpdatedAt(Instant.ofEpochMilli(getUpdatedAt()));
        return wallet;
    }
}