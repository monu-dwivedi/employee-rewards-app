package com.rewardapp.repository;

import com.rewardapp.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    public void setUp() {
        employee1 = Employee.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .department("Engineering")
                .jobTitle("Software Engineer")
                .build();

        employee2 = Employee.builder()
                .name("Jane Smith")
                .email("jane.smith@example.com")
                .department("HR")
                .jobTitle("HR Manager")
                .build();

        entityManager.persist(employee1);
        entityManager.persist(employee2);
        entityManager.flush();
    }

    @Test
    public void whenFindByEmail_thenReturnEmployee() {
        // given
        String email = "john.doe@example.com";

        // when
        Optional<Employee> found = employeeRepository.findByEmail(email);

        // then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getName()).isEqualTo(employee1.getName());
    }

    @Test
    public void whenExistsByEmail_thenReturnTrue() {
        // given
        String email = "jane.smith@example.com";

        // when
        boolean exists = employeeRepository.existsByEmail(email);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    public void whenFindByDepartmentIgnoreCase_thenReturnEmployeeList() {
        // given
        String department = "engineering";

        // when
        List<Employee> found = employeeRepository.findByDepartmentIgnoreCase(department);

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo(employee1.getName());
    }

    @Test
    public void whenFindByNameContainingIgnoreCase_thenReturnEmployeeList() {
        // given
        String namePart = "john";

        // when
        List<Employee> found = employeeRepository.findByNameContainingIgnoreCase(namePart);

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getEmail()).isEqualTo(employee1.getEmail());
    }

    @Test
    public void whenFindAllDepartments_thenReturnDepartmentList() {
        // when
        List<String> departments = employeeRepository.findAllDepartments();

        // then
        assertThat(departments).contains("Engineering", "HR");
        assertThat(departments).hasSize(2);
    }
}
