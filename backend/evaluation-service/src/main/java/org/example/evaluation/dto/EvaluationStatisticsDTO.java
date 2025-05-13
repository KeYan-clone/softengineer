package org.example.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实验评价统计数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationStatisticsDTO {

    private String experimentId;
    private Double averageRating;
    private long totalEvaluations;
    private long oneStarCount;
    private long twoStarCount;
    private long threeStarCount;
    private long fourStarCount;
    private long fiveStarCount;
}
