package com.abicoding.jcconf.speed_up_springbootest.adapter.repository;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
