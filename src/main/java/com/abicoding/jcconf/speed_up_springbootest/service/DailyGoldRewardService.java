package com.abicoding.jcconf.speed_up_springbootest.service;

import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
import com.abicoding.jcconf.speed_up_springbootest.entity.User;
import com.abicoding.jcconf.speed_up_springbootest.entity.Wallet;
import com.abicoding.jcconf.speed_up_springbootest.util.TimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class DailyGoldRewardService {

    private static final Long DAILY_GOLD_AMOUNT = 10L;

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final DailyGoldRewardRepository dailyGoldRewardRepository;

    public DailyGoldRewardService(UserRepository userRepository,
                                  WalletRepository walletRepository,
                                  DailyGoldRewardRepository dailyGoldRewardRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.dailyGoldRewardRepository = dailyGoldRewardRepository;
    }

    @Transactional
    public void claim(Long userId) {
        User user = userRepository.getById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        Instant now = Instant.now();
        if (dailyGoldRewardRepository.hasClaimed(userId, now)) {
            throw new DailyGoldenClaimedException("userId=%s".formatted(userId));
        }

        Wallet wallet = walletRepository.getByUserId(userId);
        if (wallet == null) {
            throw new WalletNotFoundException("Wallet not found");
        }

        walletRepository.addGold(userId, DAILY_GOLD_AMOUNT, now);

        DailyGoldReward reward = new DailyGoldReward();
        reward.setUserId(userId);
        reward.setRewardDate(TimeUtils.toYYYYMMDD(now));
        reward.setAmount(DAILY_GOLD_AMOUNT);
        reward.setCreatedAt(now);
        dailyGoldRewardRepository.claim(reward);
    }
}