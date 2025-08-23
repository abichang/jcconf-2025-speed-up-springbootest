package com.abicoding.jcconf.speed_up_springbootest.service;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(String message) {
        super(message);
    }
}
