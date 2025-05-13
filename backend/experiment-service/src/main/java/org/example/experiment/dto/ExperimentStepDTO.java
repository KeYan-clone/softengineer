package org.example.experiment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.experiment.model.StepType;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for ExperimentStep entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperimentStepDTO {
    private Long id;
    private Long experimentId;
    private String name;
    private String description;
    private Integer sequence;
    private StepType stepType;
    private String configuration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
