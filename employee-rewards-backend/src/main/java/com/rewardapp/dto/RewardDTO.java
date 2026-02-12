package com.rewardapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardDTO {
    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private String employeeName;
    private String employeeDepartment;

    @NotBlank(message = "Reward name is required")
    private String rewardName;

    private LocalDate dateAwarded;
    private String rewardType;
    private Integer points;
    private String description;
}
