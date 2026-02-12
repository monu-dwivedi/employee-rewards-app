package com.rewardapp.config;

import com.rewardapp.entity.Employee;
import com.rewardapp.entity.Reward;
import com.rewardapp.repository.EmployeeRepository;
import com.rewardapp.repository.RewardRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@Builder
public class DataInitializer {

    @Bean
    @Profile("!test")
    public CommandLineRunner initData(EmployeeRepository employeeRepo, RewardRepository rewardRepo) {
        return args -> {
        	System.out.println(employeeRepo.count());
            if (employeeRepo.count() == 0) {
                // Sample Employees
                Employee e1 = employeeRepo.save(Employee.builder()
                        .name("Monu Dwivedi").department("Engineering").email("monudwivedi@company.com")
                        .jobTitle("Senior Java Developer").build());
                Employee e2 = employeeRepo.save(Employee.builder()
                        .name("Ramesh Bhandari").department("Marketing").email("rameshb@company.com")
                        .jobTitle("Marketing Manager").build());
                Employee e3 = employeeRepo.save(Employee.builder()
                        .name("Shweta Arora").department("HR").email("shwetaa@company.com")
                        .jobTitle("HR Specialist").build());
                Employee e4 = employeeRepo.save(Employee.builder()
                        .name("Manas Ranjan").department("Engineering").email("manasr@company.com")
                        .jobTitle("DevOps Engineer").build());
                Employee e5 = employeeRepo.save(Employee.builder()
                        .name("Vikas Kumar").department("Sales").email("vikask@company.com")
                        .jobTitle("Sales Executive").build());

                // Sample Rewards
                rewardRepo.save(Reward.builder().employee(e1).rewardName("Employee of the Month")
                        .rewardType("Performance").dateAwarded(LocalDate.now().minusMonths(1))
                        .points(500).description("Outstanding performance in Q3").build());
                rewardRepo.save(Reward.builder().employee(e1).rewardName("Innovation Award")
                        .rewardType("Innovation").dateAwarded(LocalDate.now().minusMonths(3))
                        .points(300).description("Led the new microservices migration").build());
                rewardRepo.save(Reward.builder().employee(e2).rewardName("Best Campaign Award")
                        .rewardType("Performance").dateAwarded(LocalDate.now().minusWeeks(2))
                        .points(400).description("Delivered record-breaking Q4 campaign").build());
                rewardRepo.save(Reward.builder().employee(e3).rewardName("Team Player Award")
                        .rewardType("Teamwork").dateAwarded(LocalDate.now().minusMonths(2))
                        .points(200).description("Exceptional collaboration across teams").build());
                rewardRepo.save(Reward.builder().employee(e5).rewardName("Top Sales Award")
                        .rewardType("Customer Service").dateAwarded(LocalDate.now().minusWeeks(1))
                        .points(600).description("Exceeded sales targets by 150%").build());

            }
            System.out.println("load successfully");
        };
    }
}
