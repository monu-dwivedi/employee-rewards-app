package com.rewardapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewardapp.dto.RewardDTO;
import com.rewardapp.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
public class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    @Autowired
    private ObjectMapper objectMapper;

    private RewardDTO rewardDTO;

    @BeforeEach
    public void setUp() {
        rewardDTO = RewardDTO.builder()
                .id(1L)
                .employeeId(1L)
                .rewardName("Employee of the Month")
                .dateAwarded(LocalDate.now())
                .rewardType("Performance")
                .points(100)
                .description("Great job")
                .build();
    }

    @Test
    public void whenGetAllRewards_thenReturnJsonArray() throws Exception {
        // given
        when(rewardService.getAllRewards()).thenReturn(List.of(rewardDTO));

        // when & then
        mockMvc.perform(get("/api/rewards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].rewardName").value("Employee of the Month"));
    }

    @Test
    public void whenGetRewardById_thenReturnJson() throws Exception {
        // given
        when(rewardService.getRewardById(1L)).thenReturn(rewardDTO);

        // when & then
        mockMvc.perform(get("/api/rewards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rewardName").value("Employee of the Month"));
    }

    @Test
    public void whenAssignReward_thenReturnCreated() throws Exception {
        // given
        when(rewardService.assignReward(any(RewardDTO.class))).thenReturn(rewardDTO);

        // when & then
        mockMvc.perform(post("/api/rewards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rewardDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.rewardName").value("Employee of the Month"));
    }

    @Test
    public void whenUpdateReward_thenReturnJson() throws Exception {
        // given
        when(rewardService.updateReward(eq(1L), any(RewardDTO.class))).thenReturn(rewardDTO);

        // when & then
        mockMvc.perform(put("/api/rewards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rewardDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rewardName").value("Employee of the Month"));
    }

    @Test
    public void whenDeleteReward_thenExecuteSuccessfully() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/rewards/1"))
                .andExpect(status().isOk());

        verify(rewardService, times(1)).deleteReward(1L);
    }
}
