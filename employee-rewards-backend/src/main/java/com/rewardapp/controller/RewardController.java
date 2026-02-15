package com.rewardapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rewardapp.dto.ApiResponse;
import com.rewardapp.dto.RewardDTO;
import com.rewardapp.service.RewardService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/rewards")
@CrossOrigin(origins = "*")
@Slf4j
public class RewardController {

    private final RewardService rewardService;
    
    @Autowired
    public RewardController(RewardService rewardService) {
    	this.rewardService = rewardService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RewardDTO>>> getAllRewards() {
        log.info("REST request to get all rewards");
        return ResponseEntity.ok(ApiResponse.success(rewardService.getAllRewards()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RewardDTO>> getRewardById(@PathVariable Long id) {
        log.info("REST request to get reward with id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(rewardService.getRewardById(id)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<RewardDTO>>> getRewardsByEmployee(@PathVariable Long employeeId) {
        log.info("REST request to get rewards for employee id: {}", employeeId);
        return ResponseEntity.ok(ApiResponse.success(rewardService.getRewardsByEmployee(employeeId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RewardDTO>> assignReward(@Valid @RequestBody RewardDTO dto) {
        log.info("REST request to assign reward: {}", dto);
        RewardDTO created = rewardService.assignReward(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reward assigned successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RewardDTO>> updateReward(
            @PathVariable Long id,
            @Valid @RequestBody RewardDTO dto) {
        log.info("REST request to update reward with id: {}", id);
        RewardDTO updated = rewardService.updateReward(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Reward updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReward(@PathVariable Long id) {
        log.info("REST request to delete reward with id: {}", id);
        rewardService.deleteReward(id);
        return ResponseEntity.ok(ApiResponse.success("Reward deleted successfully", null));
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse<List<String>>> getRewardTypes() {
        log.info("REST request to get all reward types");
        return ResponseEntity.ok(ApiResponse.success(rewardService.getAllRewardTypes()));
    }
}
