package com.abicoding.jcconf.speed_up_springbootest.adapter.repository;

public class OptimisticLockException extends RuntimeException {
    public OptimisticLockException(String message) {
        super(message);
    }
}