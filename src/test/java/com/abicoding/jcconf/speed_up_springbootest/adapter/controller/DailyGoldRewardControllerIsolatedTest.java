package com.abicoding.jcconf.speed_up_springbootest.adapter.controller;

import com.abicoding.jcconf.speed_up_springbootest.adapter.repository.WalletNotFoundException;
import com.abicoding.jcconf.speed_up_springbootest.common.SystemTestBase;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DailyGoldRewardControllerIsolatedTest extends SystemTestBase {


    @Test
    void wallet_not_found() throws Exception {
        doThrow(new WalletNotFoundException("userId=999"))
                .when(dailyGoldRewardService).claim(999L);

        mockMvc.perform(post("/user/999/daily-golden"))
                .andExpect(status().isNotFound());
    }


    @Test
    void invalid_user_id() throws Exception {
        mockMvc.perform(post("/user/invalid/daily-golden"))
                .andExpect(status().isBadRequest());
    }
}