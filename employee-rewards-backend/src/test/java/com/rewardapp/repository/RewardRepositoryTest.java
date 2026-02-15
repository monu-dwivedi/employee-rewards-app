package com.rewardapp.repository;

import com.rewardapp.entity.Employee;
import com.rewardapp.entity.Reward;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RewardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RewardRepository rewardRepository;

    private Employee employee;
    private Reward reward1;
    private Reward reward2;

    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .name("Alice Wonderland")
                .email("alice@example.com")
                .department("Marketing")
                .jobTitle("Content Creator")
                .build();
        entityManager.persist(employee);

        reward1 = Reward.builder()
                .employee(employee)
                .rewardName("Employee of the Month")
                .dateAwarded(LocalDate.now().minusDays(10))
                .rewardType("Performance")
                .points(100)
                .description("Great performance in Q1")
                .build();
        entityManager.persist(reward1);

        reward2 = Reward.builder()
                .employee(employee)
                .rewardName("Team Player")
                .dateAwarded(LocalDate.now().minusDays(5))
                .rewardType("Teamwork")
                .points(50)
                .description("Helped team members")
                .build();
        entityManager.persist(reward2);
        
        entityManager.flush();
    }

    @Test
    void whenFindByEmployeeId_thenReturnRewardList() {
        // when
        List<Reward> found = rewardRepository.findByEmployeeId(employee.getId());

        // then
        assertThat(found).hasSize(2);
    }

    @Test
    void whenFindByRewardTypeIgnoreCase_thenReturnRewardList() {
        // given
        String type = "performance";

        // when
        List<Reward> found = rewardRepository.findByRewardTypeIgnoreCase(type);

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getRewardName()).isEqualTo(reward1.getRewardName());
    }

    @Test
    void whenCountByEmployeeId_thenReturnCount() {
        // when
        long count = rewardRepository.countByEmployeeId(employee.getId());

        // then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void whenSumPointsByEmployeeId_thenReturnSum() {
        // when
        Integer sum = rewardRepository.sumPointsByEmployeeId(employee.getId());

        // then
        assertThat(sum).isEqualTo(150);
    }
}
