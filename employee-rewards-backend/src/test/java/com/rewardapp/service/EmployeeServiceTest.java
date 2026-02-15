package com.rewardapp.service;

import com.rewardapp.dto.EmployeeDTO;
import com.rewardapp.entity.Employee;
import com.rewardapp.exception.DuplicateResourceException;
import com.rewardapp.exception.ResourceNotFoundException;
import com.rewardapp.repository.EmployeeRepository;
import com.rewardapp.repository.RewardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RewardRepository rewardRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    public void setUp() {
        employee = Employee.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .department("Engineering")
                .jobTitle("Software Engineer")
                .build();

        employeeDTO = EmployeeDTO.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .department("Engineering")
                .jobTitle("Software Engineer")
                .build();
    }

    @Test
    public void whenGetAllEmployees_thenReturnDtoList() {
        // given
        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        when(rewardRepository.countByEmployeeId(1L)).thenReturn(5L);
        when(rewardRepository.sumPointsByEmployeeId(1L)).thenReturn(200);

        // when
        List<EmployeeDTO> result = employeeService.getAllEmployees();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
        assertThat(result.get(0).getRewardCount()).isEqualTo(5);
        assertThat(result.get(0).getTotalPoints()).isEqualTo(200);
    }

    @Test
    public void whenGetEmployeeById_thenReturnDto() {
        // given
        when(employeeRepository.findByIdWithRewards(1L)).thenReturn(Optional.of(employee));
        when(rewardRepository.countByEmployeeId(1L)).thenReturn(5L);
        when(rewardRepository.sumPointsByEmployeeId(1L)).thenReturn(200);

        // when
        EmployeeDTO result = employeeService.getEmployeeById(1L);

        // then
        assertThat(result.getName()).isEqualTo("John Doe");
    }

    @Test
    public void whenGetEmployeeById_NotFound_thenThrowException() {
        // given
        when(employeeRepository.findByIdWithRewards(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(1L));
    }

    @Test
    public void whenCreateEmployee_thenReturnDto() {
        // given
        when(employeeRepository.existsByEmail(any())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        
        // when
        EmployeeDTO result = employeeService.createEmployee(employeeDTO);

        // then
        assertThat(result.getName()).isEqualTo("John Doe");
    }

    @Test
    public void whenCreateEmployee_DuplicateEmail_thenThrowException() {
        // given
        when(employeeRepository.existsByEmail(any())).thenReturn(true);

        // when & then
        assertThrows(DuplicateResourceException.class, () -> employeeService.createEmployee(employeeDTO));
    }

    @Test
    public void whenUpdateEmployee_thenReturnDto() {
        // given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // when
        EmployeeDTO result = employeeService.updateEmployee(1L, employeeDTO);

        // then
        assertThat(result.getName()).isEqualTo("John Doe");
    }

    @Test
    public void whenDeleteEmployee_thenExecuteSuccessfully() {
        // given
        when(employeeRepository.existsById(1L)).thenReturn(true);

        // when
        employeeService.deleteEmployee(1L);

        // then
        verify(employeeRepository, times(1)).deleteById(1L);
    }
}
