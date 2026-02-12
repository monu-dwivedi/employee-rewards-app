package com.rewardapp.service;

import com.rewardapp.dto.EmployeeDTO;
import com.rewardapp.entity.Employee;
import com.rewardapp.exception.DuplicateResourceException;
import com.rewardapp.exception.ResourceNotFoundException;
import com.rewardapp.repository.EmployeeRepository;
import com.rewardapp.repository.RewardRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RewardRepository rewardRepository;
    
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, RewardRepository rewardRepository) {
    	this.employeeRepository = employeeRepository;
    	this.rewardRepository = rewardRepository;
    }


    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findByIdWithRewards(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
        return toDTO(employee);
    }

    public EmployeeDTO createEmployee(EmployeeDTO dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Employee with email " + dto.getEmail() + " already exists");
        }
        Employee employee = Employee.builder()
                .name(dto.getName())
                .department(dto.getDepartment())
                .email(dto.getEmail())
                .jobTitle(dto.getJobTitle())
                .build();
        Employee saved = employeeRepository.save(employee);
        return toDTO(saved);
    }

    public EmployeeDTO updateEmployee(Long id, EmployeeDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        // Check email uniqueness if changed
        if (!employee.getEmail().equals(dto.getEmail()) && employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Employee with email " + dto.getEmail() + " already exists");
        }

        employee.setName(dto.getName());
        employee.setDepartment(dto.getDepartment());
        employee.setEmail(dto.getEmail());
        employee.setJobTitle(dto.getJobTitle());

        Employee updated = employeeRepository.save(employee);
        return toDTO(updated);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee", id);
        }
        employeeRepository.deleteById(id);
    }

    public List<EmployeeDTO> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartmentIgnoreCase(department)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> searchEmployees(String name) {
        return employeeRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<String> getAllDepartments() {
        return employeeRepository.findAllDepartments();
    }

    private EmployeeDTO toDTO(Employee employee) {
        int rewardCount = (int) rewardRepository.countByEmployeeId(employee.getId());
        Integer totalPoints = rewardRepository.sumPointsByEmployeeId(employee.getId());

        return EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .department(employee.getDepartment())
                .email(employee.getEmail())
                .jobTitle(employee.getJobTitle())
                .rewardCount(rewardCount)
                .totalPoints(totalPoints != null ? totalPoints : 0)
                .build();
    }
}
