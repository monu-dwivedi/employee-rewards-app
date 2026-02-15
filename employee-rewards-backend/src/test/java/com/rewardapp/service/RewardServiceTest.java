package com.rewardapp.service;

import com.rewardapp.dto.RewardDTO;
import com.rewardapp.entity.Employee;
import com.rewardapp.entity.Reward;
import com.rewardapp.exception.ResourceNotFoundException;
import com.rewardapp.repository.EmployeeRepository;
import com.rewardapp.repository.RewardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {

    @Mock
    private RewardRepository rewardRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private RewardService rewardService;

    private Employee employee;
    private Reward reward;
    private RewardDTO rewardDTO;

    @BeforeEach
    public void setUp() {
        employee = Employee.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .department("Engineering")
                .jobTitle("Software Engineer")
                .build();

        reward = Reward.builder()
                .id(1L)
                .employee(employee)
                .rewardName("Employee of the Month")
                .dateAwarded(LocalDate.now())
                .rewardType("Performance")
                .points(100)
                .description("Great job")
                .build();

        rewardDTO = RewardDTO.builder()
                .employeeId(1L)
                .rewardName("Employee of the Month")
                .dateAwarded(LocalDate.now())
                .rewardType("Performance")
                .points(100)
                .description("Great job")
                .build();
    }

    @Test
    public void whenGetAllRewards_thenReturnDtoList() {
        // given
        when(rewardRepository.findAllWithEmployee()).thenReturn(List.of(reward));

        // when
        List<RewardDTO> result = rewardService.getAllRewards();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRewardName()).isEqualTo("Employee of the Month");
    }

    @Test
    public void whenGetRewardById_thenReturnDto() {
        // given
        when(rewardRepository.findById(1L)).thenReturn(Optional.of(reward));

        // when
        RewardDTO result = rewardService.getRewardById(1L);

        // then
        assertThat(result.getRewardName()).isEqualTo("Employee of the Month");
    }

    @Test
    public void whenGetRewardsByEmployee_thenReturnDtoList() {
        // given
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(rewardRepository.findByEmployeeIdWithEmployee(1L)).thenReturn(List.of(reward));

        // when
        List<RewardDTO> result = rewardService.getRewardsByEmployee(1L);

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    public void whenAssignReward_thenReturnDto() {
        // given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(rewardRepository.save(any(Reward.class))).thenReturn(reward);

        // when
        RewardDTO result = rewardService.assignReward(rewardDTO);

        // then
        assertThat(result.getPoints()).isEqualTo(100);
    }

    @Test
    public void whenDeleteReward_thenExecuteSuccessfully() {
        // given
        when(rewardRepository.existsById(1L)).thenReturn(true);

        // when
        rewardService.deleteReward(1L);

        // then
        verify(rewardRepository, times(1)).deleteById(1L);
    }
}
