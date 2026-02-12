package com.rewardapp.repository;

import com.rewardapp.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {

    List<Reward> findByEmployeeId(Long employeeId);

    List<Reward> findByRewardTypeIgnoreCase(String rewardType);

    @Query("SELECT r FROM Reward r JOIN FETCH r.employee ORDER BY r.dateAwarded DESC")
    List<Reward> findAllWithEmployee();

    @Query("SELECT r FROM Reward r JOIN FETCH r.employee WHERE r.employee.id = :employeeId ORDER BY r.dateAwarded DESC")
    List<Reward> findByEmployeeIdWithEmployee(Long employeeId);

    @Query("SELECT COUNT(r) FROM Reward r WHERE r.employee.id = :employeeId")
    long countByEmployeeId(Long employeeId);

    @Query("SELECT SUM(r.points) FROM Reward r WHERE r.employee.id = :employeeId")
    Integer sumPointsByEmployeeId(Long employeeId);
}
