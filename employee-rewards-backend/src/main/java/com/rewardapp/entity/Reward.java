package com.rewardapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rewards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;

    @NotBlank(message = "Reward name is required")
    @Column(name = "reward_name", nullable = false)
    private String rewardName;

    @NotNull(message = "Award date is required")
    @Column(name = "date_awarded", nullable = false)
    private LocalDate dateAwarded;

    @Column(name = "reward_type")
    private String rewardType;

    @Column(name = "points")
    private Integer points;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (dateAwarded == null) {
            dateAwarded = LocalDate.now();
        }
    }
}
