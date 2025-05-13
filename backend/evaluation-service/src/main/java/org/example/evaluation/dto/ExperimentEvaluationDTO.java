package org.example.evaluation.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * 创建实验评价的数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentEvaluationDTO {

    @NotBlank(message = "实验ID不能为空")
    private String experimentId;

    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Integer rating;

    @Size(max = 1000, message = "评论长度不能超过1000字符")
    private String comment;

    private Set<CriteriaRatingDTO> criteriaRatings = new HashSet<>();
}
