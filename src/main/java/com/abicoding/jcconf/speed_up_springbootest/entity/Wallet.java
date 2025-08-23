package com.abicoding.jcconf.speed_up_springbootest.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class Wallet {
    private Long userId;
    private Long gold;
    private Instant createdAt;
    private Instant updatedAt;
}