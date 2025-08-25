package com.abicoding.jcconf.speed_up_springbootest.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Wallet {
    private final Long version;
    private Long userId;
    private Long gold;
    private Instant createdAt;
    private Instant updatedAt;

    public static Wallet restore(Long userId, Long gold, Long version, Instant createdAt, Instant updatedAt) {
        return new Wallet(version, userId, gold, createdAt, updatedAt);
    }

    public void addGold(Long amount, Instant updatedAt) {
        this.gold += amount;
        this.updatedAt = updatedAt;
    }
}