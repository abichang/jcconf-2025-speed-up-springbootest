package com.abicoding.jcconf.speed_up_springbootest.service;

public class OptimisticLockException extends RuntimeException {
    public OptimisticLockException(String message) {
        super(message);
    }
}