package com.rewardapp.controller;

import com.rewardapp.dto.ApiResponse;
import com.rewardapp.dto.RewardDTO;
import com.rewardapp.service.EmployeeService;
import com.rewardapp.service.RewardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@CrossOrigin(origins = "*")
public class RewardController {

    private final RewardService rewardService;
    
    @Autowired
    public RewardController(RewardService rewardService) {
    	this.rewardService = rewardService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RewardDTO>>> getAllRewards() {
        return ResponseEntity.ok(ApiResponse.success(rewardService.getAllRewards()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RewardDTO>> getRewardById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(rewardService.getRewardById(id)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<RewardDTO>>> getRewardsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(ApiResponse.success(rewardService.getRewardsByEmployee(employeeId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RewardDTO>> assignReward(@Valid @RequestBody RewardDTO dto) {
        RewardDTO created = rewardService.assignReward(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reward assigned successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RewardDTO>> updateReward(
            @PathVariable Long id,
            @Valid @RequestBody RewardDTO dto) {
        RewardDTO updated = rewardService.updateReward(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Reward updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReward(@PathVariable Long id) {
        rewardService.deleteReward(id);
        return ResponseEntity.ok(ApiResponse.success("Reward deleted successfully", null));
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse<List<String>>> getRewardTypes() {
        return ResponseEntity.ok(ApiResponse.success(rewardService.getAllRewardTypes()));
    }
}
