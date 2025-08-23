package com.abicoding.jcconf.speed_up_springbootest.service;

import com.abicoding.jcconf.speed_up_springbootest.entity.User;

public interface UserRepository {
    User getById(Long userId);
}