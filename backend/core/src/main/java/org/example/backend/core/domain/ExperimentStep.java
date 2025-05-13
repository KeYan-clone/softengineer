package org.example.backend.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

/**
 * Experiment step domain model
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "experiment_steps")
public class ExperimentStep {
    /**
     * Step ID
     */
    @Id
    private String id;
    
    /**
     * Step number
     */
    @Column(nullable = false)
    private Integer stepNumber;
    
    /**
     * Step title
     */
    @Column(nullable = false)
    private String title;
    
    /**
     * Step content
     */
    @Column(length = 5000)
    private String content;
    
    /**
     * Associated experiment
     * Exclude from toString to prevent infinite recursion
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_id")
    private Experiment experiment;
}
