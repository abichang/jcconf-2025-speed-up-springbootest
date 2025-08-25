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

    public static WalletDbDto from(Wallet wallet) {
        WalletDbDto dto = new WalletDbDto();
        dto.setUserId(wallet.getUserId());
        dto.setGold(wallet.getGold());
        dto.setVersion(wallet.getVersion());
        dto.setCreatedAt(wallet.getCreatedAt().toEpochMilli());
        dto.setUpdatedAt(wallet.getUpdatedAt().toEpochMilli());
        return dto;
    }

    public Wallet toEntity() {
        return Wallet.restore(
                getUserId(),
                getGold(),
                getVersion(),
                Instant.ofEpochMilli(getCreatedAt()),
                Instant.ofEpochMilli(getUpdatedAt())
        );
    }
}