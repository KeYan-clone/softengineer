package org.example.backend.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

/**
 * Evaluation domain model
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evaluations")
public class Evaluation {
    /**
     * Evaluation ID
     */
    @Id
    private String id;
    
    /**
     * Experiment ID
     */
    @Column(name = "experiment_id", nullable = false)
    private String experimentId;
    
    /**
     * User ID (evaluator)
     */
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    /**
     * Rating score (1-5)
     */
    @Column(nullable = false)
    private Integer rating;
    
    /**
     * Evaluation content
     */
    @Column(length = 1000)
    private String content;
    
    /**
     * Creation time
     */
    private LocalDateTime createTime;
    
    /**
     * Update time
     */
    private LocalDateTime updateTime;
}
