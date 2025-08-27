package com.abicoding.jcconf.speed_up_springbootest.common;

import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldRewardRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
public abstract class SystemTestBase {

    @SpyBean
    protected DailyGoldRewardRepository dailyGoldRewardRepository;
}
