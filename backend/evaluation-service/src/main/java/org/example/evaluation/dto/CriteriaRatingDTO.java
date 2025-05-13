package org.example.evaluation.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评价标准评分的数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaRatingDTO {

    @NotBlank(message = "评价标准名称不能为空")
    private String criteriaName;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Integer rating;

    private String comment;
}
