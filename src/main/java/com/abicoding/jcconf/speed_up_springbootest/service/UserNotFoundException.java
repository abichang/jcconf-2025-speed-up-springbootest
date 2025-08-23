package com.abicoding.jcconf.speed_up_springbootest.service;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
