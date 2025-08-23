package com.abicoding.jcconf.speed_up_springbootest.service;

import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
import com.abicoding.jcconf.speed_up_springbootest.entity.RewardDate;
import com.abicoding.jcconf.speed_up_springbootest.entity.User;
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
    private final TimeUtils timeUtils;

    public DailyGoldRewardService(UserRepository userRepository,
                                  WalletRepository walletRepository,
                                  DailyGoldRewardRepository dailyGoldRewardRepository,
                                  TimeUtils timeUtils) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.dailyGoldRewardRepository = dailyGoldRewardRepository;
        this.timeUtils = timeUtils;
    }

    @Transactional
    public void claim(Long userId) {

        Instant now = timeUtils.now();
        User user = userRepository.getById(userId);
        RewardDate rewardDate = RewardDate.create(now);

        if (dailyGoldRewardRepository.hasClaimed(user, rewardDate)) {
            throw new DailyGoldenClaimedException("userId=%s".formatted(user.getId()));
        }

        walletRepository.addGold(user.getId(), DAILY_GOLD_AMOUNT, now);

        DailyGoldReward reward = new DailyGoldReward();
        reward.setUserId(user.getId());
        reward.setRewardDate(rewardDate);
        reward.setAmount(DAILY_GOLD_AMOUNT);
        reward.setCreatedAt(now);
        dailyGoldRewardRepository.claim(reward);
    }
}