package com.abicoding.jcconf.speed_up_springbootest.adapter.repository;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(String message) {
        super(message);
    }
}
