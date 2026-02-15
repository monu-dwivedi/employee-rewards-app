package com.rewardapp.service;

import com.rewardapp.dto.RewardDTO;
import com.rewardapp.entity.Employee;
import com.rewardapp.entity.Reward;
import com.rewardapp.exception.ResourceNotFoundException;
import com.rewardapp.repository.EmployeeRepository;
import com.rewardapp.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RewardService {

    private final RewardRepository rewardRepository;
    private final EmployeeRepository employeeRepository;
    
    @Autowired
    public RewardService(EmployeeRepository employeeRepository, RewardRepository rewardRepository) {
    	this.employeeRepository = employeeRepository;
    	this.rewardRepository = rewardRepository;
    }


    public List<RewardDTO> getAllRewards() {
        log.info("Fetching all rewards");
        List<RewardDTO> rewards = rewardRepository.findAllWithEmployee()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        log.info("Found {} rewards", rewards.size());
        return rewards;
    }

    public RewardDTO getRewardById(Long id) {
        log.info("Fetching reward with id: {}", id);
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Reward not found with id: {}", id);
                    return new ResourceNotFoundException("Reward", id);
                });
        return toDTO(reward);
    }

    public List<RewardDTO> getRewardsByEmployee(Long employeeId) {
        log.info("Fetching rewards for employee id: {}", employeeId);
        if (!employeeRepository.existsById(employeeId)) {
            log.error("Employee not found with id: {}", employeeId);
            throw new ResourceNotFoundException("Employee", employeeId);
        }
        return rewardRepository.findByEmployeeIdWithEmployee(employeeId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RewardDTO assignReward(RewardDTO dto) {
        log.info("Assigning reward '{}' to employee id: {}", dto.getRewardName(), dto.getEmployeeId());
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", dto.getEmployeeId());
                    return new ResourceNotFoundException("Employee", dto.getEmployeeId());
                });

        Reward reward = Reward.builder()
                .employee(employee)
                .rewardName(dto.getRewardName())
                .dateAwarded(dto.getDateAwarded() != null ? dto.getDateAwarded() : LocalDate.now())
                .rewardType(dto.getRewardType())
                .points(dto.getPoints())
                .description(dto.getDescription())
                .build();

        Reward saved = rewardRepository.save(reward);
        log.info("Reward assigned successfully with id: {}", saved.getId());
        return toDTO(saved);
    }

    public RewardDTO updateReward(Long id, RewardDTO dto) {
        log.info("Updating reward with id: {}", id);
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Reward not found with id: {}", id);
                    return new ResourceNotFoundException("Reward", id);
                });

        if (!reward.getEmployee().getId().equals(dto.getEmployeeId())) {
            log.info("Updating employee for reward id: {} to employee id: {}", id, dto.getEmployeeId());
            Employee newEmployee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> {
                        log.error("Employee not found with id: {}", dto.getEmployeeId());
                        return new ResourceNotFoundException("Employee", dto.getEmployeeId());
                    });
            reward.setEmployee(newEmployee);
        }

        reward.setRewardName(dto.getRewardName());
        reward.setDateAwarded(dto.getDateAwarded() != null ? dto.getDateAwarded() : LocalDate.now());
        reward.setRewardType(dto.getRewardType());
        reward.setPoints(dto.getPoints());
        reward.setDescription(dto.getDescription());

        Reward updated = rewardRepository.save(reward);
        log.info("Reward updated successfully with id: {}", updated.getId());
        return toDTO(updated);
    }

    public void deleteReward(Long id) {
        log.info("Deleting reward with id: {}", id);
        if (!rewardRepository.existsById(id)) {
            log.error("Reward not found with id: {}", id);
            throw new ResourceNotFoundException("Reward", id);
        }
        rewardRepository.deleteById(id);
        log.info("Reward deleted successfully with id: {}", id);
    }

    public List<String> getAllRewardTypes() {
        return List.of("Employee of the Month", "Performance",
                "Innovation", "Teamwork", "Leadership",
                "Customer Service", "Most Improved", "Best New Employee");
    }

    private RewardDTO toDTO(Reward reward) {
        return RewardDTO.builder()
                .id(reward.getId())
                .employeeId(reward.getEmployee().getId())
                .employeeName(reward.getEmployee().getName())
                .employeeDepartment(reward.getEmployee().getDepartment())
                .rewardName(reward.getRewardName())
                .dateAwarded(reward.getDateAwarded())
                .rewardType(reward.getRewardType())
                .points(reward.getPoints())
                .description(reward.getDescription())
                .build();
    }
}
