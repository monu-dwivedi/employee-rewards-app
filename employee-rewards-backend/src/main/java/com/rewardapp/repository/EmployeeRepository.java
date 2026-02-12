package com.rewardapp.repository;

import com.rewardapp.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Employee> findByDepartmentIgnoreCase(String department);

    List<Employee> findByNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT e.department FROM Employee e ORDER BY e.department")
    List<String> findAllDepartments();

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.rewards WHERE e.id = :id")
    Optional<Employee> findByIdWithRewards(Long id);
}
