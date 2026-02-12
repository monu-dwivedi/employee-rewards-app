package com.rewardapp.service;

import com.rewardapp.dto.RewardDTO;
import com.rewardapp.entity.Employee;
import com.rewardapp.entity.Reward;
import com.rewardapp.exception.ResourceNotFoundException;
import com.rewardapp.repository.EmployeeRepository;
import com.rewardapp.repository.RewardRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RewardService {

    private final RewardRepository rewardRepository;
    private final EmployeeRepository employeeRepository;
    
    @Autowired
    public RewardService(EmployeeRepository employeeRepository, RewardRepository rewardRepository) {
    	this.employeeRepository = employeeRepository;
    	this.rewardRepository = rewardRepository;
    }


    public List<RewardDTO> getAllRewards() {
        return rewardRepository.findAllWithEmployee()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RewardDTO getRewardById(Long id) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reward", id));
        return toDTO(reward);
    }

    public List<RewardDTO> getRewardsByEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee", employeeId);
        }
        return rewardRepository.findByEmployeeIdWithEmployee(employeeId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RewardDTO assignReward(RewardDTO dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", dto.getEmployeeId()));

        Reward reward = Reward.builder()
                .employee(employee)
                .rewardName(dto.getRewardName())
                .dateAwarded(dto.getDateAwarded() != null ? dto.getDateAwarded() : LocalDate.now())
                .rewardType(dto.getRewardType())
                .points(dto.getPoints())
                .description(dto.getDescription())
                .build();

        Reward saved = rewardRepository.save(reward);
        return toDTO(saved);
    }

    public RewardDTO updateReward(Long id, RewardDTO dto) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reward", id));

        if (!reward.getEmployee().getId().equals(dto.getEmployeeId())) {
            Employee newEmployee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", dto.getEmployeeId()));
            reward.setEmployee(newEmployee);
        }

        reward.setRewardName(dto.getRewardName());
        reward.setDateAwarded(dto.getDateAwarded() != null ? dto.getDateAwarded() : LocalDate.now());
        reward.setRewardType(dto.getRewardType());
        reward.setPoints(dto.getPoints());
        reward.setDescription(dto.getDescription());

        Reward updated = rewardRepository.save(reward);
        return toDTO(updated);
    }

    public void deleteReward(Long id) {
        if (!rewardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reward", id);
        }
        rewardRepository.deleteById(id);
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
