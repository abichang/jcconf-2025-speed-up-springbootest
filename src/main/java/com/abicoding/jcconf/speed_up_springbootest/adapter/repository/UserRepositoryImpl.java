package com.abicoding.jcconf.speed_up_springbootest.adapter.repository;

import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.UserDbDto;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.UserMapper;
import com.abicoding.jcconf.speed_up_springbootest.entity.User;
import com.abicoding.jcconf.speed_up_springbootest.service.UserNotFoundException;
import com.abicoding.jcconf.speed_up_springbootest.service.UserRepository;
import org.springframework.stereotype.Repository;

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
            throw new UserNotFoundException("userId=%s".formatted(userId));
        }
        return userDbDto.toEntity();
    }

}