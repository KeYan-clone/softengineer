package org.example.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 实验评价响应的数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentEvaluationResponseDTO {

    private Long id;
    private String experimentId;
    private String userId;
    private Integer rating;
    private String comment;
    private java.util.Set<CriteriaRatingDTO> criteriaRatings;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
