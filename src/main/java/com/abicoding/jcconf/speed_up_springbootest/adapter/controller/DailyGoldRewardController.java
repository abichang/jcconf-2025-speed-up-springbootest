package com.abicoding.jcconf.speed_up_springbootest.adapter.controller;

import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldRewardService;
import org.springframework.http.ResponseEntity;
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
        dailyGoldRewardService.claim(userId);
        return ResponseEntity.ok().build();
    }
}