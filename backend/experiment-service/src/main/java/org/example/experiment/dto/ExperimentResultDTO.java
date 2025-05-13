package org.example.experiment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.experiment.model.ResultStatus;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for ExperimentResult entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperimentResultDTO {
    private Long id;
    private Long experimentId;
    private String executionId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ResultStatus status;
    private String resultData;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
