package com.abicoding.jcconf.speed_up_springbootest.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class User {
    private Long id;
    private String username;
    private Instant createdAt;
    private Instant updatedAt;
}