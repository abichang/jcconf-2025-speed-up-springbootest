package com.abicoding.jcconf.speed_up_springbootest.common;

import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldRewardRepository;
import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldRewardService;
import com.abicoding.jcconf.speed_up_springbootest.util.TimeUtils;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class SystemTestBase {

    @SpyBean
    protected DailyGoldRewardRepository dailyGoldRewardRepository;
    @SpyBean
    protected DailyGoldRewardService dailyGoldRewardService;
    @Autowired
    protected MockMvc mockMvc;
    @SpyBean
    protected TimeUtils timeUtils;
    @Autowired
    private GetAllTableNamesMapper getAllTableNamesMapper;
    @Autowired
    private TruncateTableMapper truncateTableMapper;


    @AfterEach
    void baseTearDown() {
        String[] allTableNames = getAllTableNamesMapper.getAllTableNames();
        for (String tableName : allTableNames) {
            if (!"flyway_schema_history".equalsIgnoreCase(tableName)) {
                truncateTableMapper.truncateTable(tableName);
            }
        }
    }
}
