package com.abicoding.jcconf.speed_up_springbootest.adapter.controller;

import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldRewardService;
import com.abicoding.jcconf.speed_up_springbootest.service.DailyGoldenClaimedException;
import com.abicoding.jcconf.speed_up_springbootest.service.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyGoldRewardController.class)
class DailyGoldRewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DailyGoldRewardService dailyGoldRewardService;

    @Test
    void claim_all_ok() throws Exception {
        doNothing().when(dailyGoldRewardService).claim(1L);

        mockMvc.perform(post("/user/1/daily-golden"))
                .andExpect(status().isOk());
    }

    @Test
    void duplicate_claim() throws Exception {
        doThrow(new DailyGoldenClaimedException("userId=1"))
                .when(dailyGoldRewardService).claim(1L);

        mockMvc.perform(post("/user/1/daily-golden"))
                .andExpect(status().isConflict());
    }

    @Test
    void user_not_found() throws Exception {
        doThrow(new UserNotFoundException("userId=999"))
                .when(dailyGoldRewardService).claim(999L);

        mockMvc.perform(post("/user/999/daily-golden"))
                .andExpect(status().isNotFound());
    }

    @Test
    void can_claim_again_after_utc_midnight() throws Exception {
        doNothing().when(dailyGoldRewardService).claim(1L);

        mockMvc.perform(post("/user/1/daily-golden"))
                .andExpect(status().isOk());
    }

    @Test
    void invalid_user_id() throws Exception {
        mockMvc.perform(post("/user/invalid/daily-golden"))
                .andExpect(status().isBadRequest());
    }
}