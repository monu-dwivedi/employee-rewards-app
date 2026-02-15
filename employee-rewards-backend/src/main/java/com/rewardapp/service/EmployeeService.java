package com.rewardapp.service;

import com.rewardapp.dto.EmployeeDTO;
import com.rewardapp.entity.Employee;
import com.rewardapp.exception.DuplicateResourceException;
import com.rewardapp.exception.ResourceNotFoundException;
import com.rewardapp.repository.EmployeeRepository;
import com.rewardapp.repository.RewardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RewardRepository rewardRepository;
    
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, RewardRepository rewardRepository) {
    	this.employeeRepository = employeeRepository;
    	this.rewardRepository = rewardRepository;
    }


    public List<EmployeeDTO> getAllEmployees() {
        log.info("Fetching all employees");
        List<EmployeeDTO> employees = employeeRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        log.info("Found {} employees", employees.size());
        return employees;
    }

    public EmployeeDTO getEmployeeById(Long id) {
        log.info("Fetching employee with id: {}", id);
        Employee employee = employeeRepository.findByIdWithRewards(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee", id);
                });
        return toDTO(employee);
    }

    public EmployeeDTO createEmployee(EmployeeDTO dto) {
        log.info("Creating new employee with email: {}", dto.getEmail());
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            log.warn("Employee with email {} already exists", dto.getEmail());
            throw new DuplicateResourceException("Employee with email " + dto.getEmail() + " already exists");
        }
        Employee employee = Employee.builder()
                .name(dto.getName())
                .department(dto.getDepartment())
                .email(dto.getEmail())
                .jobTitle(dto.getJobTitle())
                .build();
        Employee saved = employeeRepository.save(employee);
        log.info("Employee created successfully with id: {}", saved.getId());
        return toDTO(saved);
    }

    public EmployeeDTO updateEmployee(Long id, EmployeeDTO dto) {
        log.info("Updating employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee", id);
                });

        // Check email uniqueness if changed
        if (!employee.getEmail().equals(dto.getEmail()) && employeeRepository.existsByEmail(dto.getEmail())) {
            log.warn("Email {} is already taken by another employee", dto.getEmail());
            throw new DuplicateResourceException("Employee with email " + dto.getEmail() + " already exists");
        }

        employee.setName(dto.getName());
        employee.setDepartment(dto.getDepartment());
        employee.setEmail(dto.getEmail());
        employee.setJobTitle(dto.getJobTitle());

        Employee updated = employeeRepository.save(employee);
        log.info("Employee updated successfully with id: {}", updated.getId());
        return toDTO(updated);
    }

    public void deleteEmployee(Long id) {
        log.info("Deleting employee with id: {}", id);
        if (!employeeRepository.existsById(id)) {
            log.error("Employee not found with id: {}", id);
            throw new ResourceNotFoundException("Employee", id);
        }
        employeeRepository.deleteById(id);
        log.info("Employee deleted successfully with id: {}", id);
    }

    public List<EmployeeDTO> getEmployeesByDepartment(String department) {
        log.info("Fetching employees in department: {}", department);
        List<EmployeeDTO> employees = employeeRepository.findByDepartmentIgnoreCase(department)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        log.info("Found {} employees in department {}", employees.size(), department);
        return employees;
    }

    public List<EmployeeDTO> searchEmployees(String name) {
        log.info("Searching employees with name containing: {}", name);
        return employeeRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<String> getAllDepartments() {
        log.info("Fetching all departments");
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
