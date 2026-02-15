package com.rewardapp.controller;

import com.rewardapp.dto.ApiResponse;
import com.rewardapp.dto.EmployeeDTO;
import com.rewardapp.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;
    
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
    	this.employeeService = employeeService;
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getAllEmployees() {
        log.info("REST request to get all employees");
        return ResponseEntity.ok(ApiResponse.success(employeeService.getAllEmployees()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> getEmployeeById(@PathVariable Long id) {
        log.info("REST request to get employee with id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(employeeService.getEmployeeById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDTO>> createEmployee(@Valid @RequestBody EmployeeDTO dto) {
        log.info("REST request to create employee: {}", dto);
        EmployeeDTO created = employeeService.createEmployee(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDTO>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO dto) {
        log.info("REST request to update employee with id: {}", id);
        EmployeeDTO updated = employeeService.updateEmployee(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        log.info("REST request to delete employee with id: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully", null));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> getByDepartment(@PathVariable String department) {
        log.info("REST request to get employees by department: {}", department);
        return ResponseEntity.ok(ApiResponse.success(employeeService.getEmployeesByDepartment(department)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<EmployeeDTO>>> searchEmployees(@RequestParam String name) {
        log.info("REST request to search employees by name: {}", name);
        return ResponseEntity.ok(ApiResponse.success(employeeService.searchEmployees(name)));
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<String>>> getAllDepartments() {
        log.info("REST request to get all departments");
        return ResponseEntity.ok(ApiResponse.success(employeeService.getAllDepartments()));
    }
}
