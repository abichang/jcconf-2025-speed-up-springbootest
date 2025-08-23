package com.abicoding.jcconf.speed_up_springbootest.service;

import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.UserDbDto;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.UserMapper;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.WalletDbDto;
import com.abicoding.jcconf.speed_up_springbootest.adapter.mapper.WalletMapper;
import com.abicoding.jcconf.speed_up_springbootest.entity.DailyGoldReward;
import com.abicoding.jcconf.speed_up_springbootest.entity.RewardDate;
import com.abicoding.jcconf.speed_up_springbootest.entity.User;
import com.abicoding.jcconf.speed_up_springbootest.entity.Wallet;
import com.abicoding.jcconf.speed_up_springbootest.util.TimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
class DailyGoldRewardServiceTransactionalTest {

    @Autowired
    private DailyGoldRewardService dailyGoldRewardService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WalletMapper walletMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private DailyGoldRewardRepository dailyGoldRewardRepository;

    @MockBean
    private TimeUtils timeUtils;

    @BeforeEach
    void setUp() {
        // 清理並設置測試資料
        setupTestData();
    }

    @Test
    void claim_fail_wallet_rollback() {
        Long userId = 1L;
        Instant now = Instant.parse("2024-01-15T10:00:00Z");
        Long initialGold = 500L;
        
        // 準備測試資料
        User user = new User();
        user.setId(userId);
        
        // Mock 設定
        doReturn(now).when(timeUtils).now();
        doReturn(user).when(userRepository).getById(userId);
        doReturn(false).when(dailyGoldRewardRepository).hasClaimed(eq(user), any(RewardDate.class));
        
        // 讓 dailyGoldRewardRepository.claim() 拋出異常
        doThrow(new RuntimeException("Database error"))
                .when(dailyGoldRewardRepository).claim(any(DailyGoldReward.class));
        
        // 獲取原始錢包金額
        Wallet originalWallet = walletRepository.getByUserId(userId);
        Long originalGold = originalWallet.getGold();
        assertThat(originalGold).isEqualTo(initialGold);
        
        // 執行測試 - 應該拋出異常
        assertThrows(RuntimeException.class, () -> {
            dailyGoldRewardService.claim(userId);
        });
        
        // 驗證錢包金額已回滾到原始狀態（事務回滾）
        Wallet walletAfterRollback = walletRepository.getByUserId(userId);
        assertThat(walletAfterRollback.getGold()).isEqualTo(originalGold);
    }
    
    private void setupTestData() {
        Long userId = 1L;
        Long initialGold = 500L;
        Instant now = Instant.now();
        long nowEpochMilli = now.toEpochMilli();
        
        // 插入測試用戶資料
        UserDbDto userDbDto = new UserDbDto();
        userDbDto.setId(userId);
        userDbDto.setUsername("test-user");
        userDbDto.setCreatedAt(nowEpochMilli);
        userDbDto.setUpdatedAt(nowEpochMilli);
        userMapper.insert(userDbDto);
        
        // 插入測試錢包資料
        WalletDbDto walletDbDto = new WalletDbDto();
        walletDbDto.setUserId(userId);
        walletDbDto.setGold(initialGold);
        walletDbDto.setCreatedAt(nowEpochMilli);
        walletDbDto.setUpdatedAt(nowEpochMilli);
        walletMapper.insert(walletDbDto);
    }
}