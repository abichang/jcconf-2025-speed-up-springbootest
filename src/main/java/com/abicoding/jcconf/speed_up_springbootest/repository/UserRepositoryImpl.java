package com.abicoding.jcconf.speed_up_springbootest.repository;

import com.abicoding.jcconf.speed_up_springbootest.entity.User;
import com.abicoding.jcconf.speed_up_springbootest.mapper.UserDbDto;
import com.abicoding.jcconf.speed_up_springbootest.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    public UserRepositoryImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getById(Long userId) {
        UserDbDto userDbDto = userMapper.selectById(userId);
        if (userDbDto == null) {
            return null;
        }
        return convertToEntity(userDbDto);
    }

    private User convertToEntity(UserDbDto userDbDto) {
        User user = new User();
        user.setId(userDbDto.getId());
        user.setUsername(userDbDto.getUsername());
        user.setCreatedAt(Instant.ofEpochMilli(userDbDto.getCreatedAt()));
        user.setUpdatedAt(Instant.ofEpochMilli(userDbDto.getUpdatedAt()));
        return user;
    }
}