package com.abicoding.jcconf.speed_up_springbootest.dto;

import lombok.Data;

@Data
public class UserDbDto {
    private Long id;
    private String username;
    private Long createdAt;
    private Long updatedAt;
}