package org.example.evaluation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * 评价标准评分，作为嵌入式对象存储
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriteriaRating {

    @Column(nullable = false)
    private String criteriaName;

    @Column(nullable = false)
    private Integer rating;

    private String comment;
}
