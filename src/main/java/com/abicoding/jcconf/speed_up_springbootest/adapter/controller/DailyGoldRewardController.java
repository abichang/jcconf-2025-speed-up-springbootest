package com.abicoding.jcconf.speed_up_springbootest.adapter.controller;

import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldRewardService;
import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldenClaimedException;
import com.abicoding.jcconf.speed_up_springbootest.service.UserNotFoundException;
import com.abicoding.jcconf.speed_up_springbootest.service.WalletNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DailyGoldRewardController {

    private final DailyGoldRewardService dailyGoldRewardService;

    public DailyGoldRewardController(DailyGoldRewardService dailyGoldRewardService) {
        this.dailyGoldRewardService = dailyGoldRewardService;
    }

    @PostMapping("/user/{userId}/daily-golden")
    public ResponseEntity<Void> claimDailyGold(@PathVariable Long userId) {
        try {
            dailyGoldRewardService.claim(userId);
            return ResponseEntity.ok().build();
        } catch (DailyGoldenClaimedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (UserNotFoundException | WalletNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Void> handleNumberFormatException(NumberFormatException e) {
        return ResponseEntity.badRequest().build();
    }
}