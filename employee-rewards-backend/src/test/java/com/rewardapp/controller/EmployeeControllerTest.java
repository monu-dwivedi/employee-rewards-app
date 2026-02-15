package com.rewardapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewardapp.dto.EmployeeDTO;
import com.rewardapp.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeDTO employeeDTO;

    @BeforeEach
    public void setUp() {
        employeeDTO = EmployeeDTO.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .department("Engineering")
                .jobTitle("Software Engineer")
                .build();
    }

    @Test
    public void whenGetAllEmployees_thenReturnJsonArray() throws Exception {
        // given
        when(employeeService.getAllEmployees()).thenReturn(List.of(employeeDTO));

        // when & then
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("John Doe"));
    }

    @Test
    public void whenGetEmployeeById_thenReturnJson() throws Exception {
        // given
        when(employeeService.getEmployeeById(1L)).thenReturn(employeeDTO);

        // when & then
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    public void whenCreateEmployee_thenReturnCreated() throws Exception {
        // given
        when(employeeService.createEmployee(any(EmployeeDTO.class))).thenReturn(employeeDTO);

        // when & then
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    public void whenUpdateEmployee_thenReturnJson() throws Exception {
        // given
        when(employeeService.updateEmployee(eq(1L), any(EmployeeDTO.class))).thenReturn(employeeDTO);

        // when & then
        mockMvc.perform(put("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    public void whenDeleteEmployee_thenExecuteSuccessfully() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).deleteEmployee(1L);
    }
}
