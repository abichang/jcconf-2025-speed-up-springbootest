package com.abicoding.jcconf.speed_up_springbootest.adapter.mapper;

import lombok.Data;

@Data
public class WalletDbDto {
    private Long userId;
    private Long gold;
    private Long createdAt;
    private Long updatedAt;
}