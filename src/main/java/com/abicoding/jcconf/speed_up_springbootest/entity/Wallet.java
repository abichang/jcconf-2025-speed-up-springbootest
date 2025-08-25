package com.abicoding.jcconf.speed_up_springbootest.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class Wallet {
    private Long userId;
    private Long gold;
    private Long version;
    private Instant createdAt;
    private Instant updatedAt;

    public void addGold(Long amount, Instant updatedAt) {
        this.gold += amount;
        this.version += 1;
        this.updatedAt = updatedAt;
    }
}