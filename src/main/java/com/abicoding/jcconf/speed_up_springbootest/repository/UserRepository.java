package com.abicoding.jcconf.speed_up_springbootest.repository;

import com.abicoding.jcconf.speed_up_springbootest.entity.User;

public interface UserRepository {
    User getById(Long userId);
}