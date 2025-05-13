package org.example.experiment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.experiment.model.ExperimentStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for Experiment entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperimentDTO {
    private Long id;
    private String name;
    private String description;
    private String userId;
    private ExperimentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ExperimentStepDTO> steps = new ArrayList<>();
}
