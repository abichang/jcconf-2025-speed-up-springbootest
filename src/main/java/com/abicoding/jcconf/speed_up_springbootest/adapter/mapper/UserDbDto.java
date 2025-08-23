package com.abicoding.jcconf.speed_up_springbootest.adapter.mapper;

import com.abicoding.jcconf.speed_up_springbootest.entity.User;
import lombok.Data;

import java.time.Instant;

@Data
public class UserDbDto {
    private Long id;
    private String username;
    private Long createdAt;
    private Long updatedAt;

    public User toEntity() {
        User user = new User();
        user.setId(getId());
        user.setUsername(getUsername());
        user.setCreatedAt(Instant.ofEpochMilli(getCreatedAt()));
        user.setUpdatedAt(Instant.ofEpochMilli(getUpdatedAt()));
        return user;
    }
}