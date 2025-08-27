package com.abicoding.jcconf.speed_up_springbootest.common;

import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldRewardRepository;
import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldRewardService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class SystemTestBase {

    @SpyBean
    protected DailyGoldRewardRepository dailyGoldRewardRepository;
    @SpyBean
    protected DailyGoldRewardService dailyGoldRewardService;
}
