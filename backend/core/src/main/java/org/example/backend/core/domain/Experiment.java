package org.example.backend.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Experiment domain model
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "experiments")
public class Experiment {
    /**
     * Experiment ID
     */
    @Id
    private String id;
    
    /**
     * Experiment name
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * Experiment description
     */
    @Column(length = 2000)
    private String description;
    
    /**
     * Creator ID
     */
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    /**
     * Experiment status
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExperimentStatus status;
    
    /**
     * Creation time
     */
    private LocalDateTime createTime;
    
    /**
     * Update time
     */
    private LocalDateTime updateTime;
    
    /**
     * Experiment steps
     */
    @OneToMany(mappedBy = "experiment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ExperimentStep> steps = new ArrayList<>();
    
    /**
     * Add an experiment step
     */
    public void addStep(ExperimentStep step) {
        steps.add(step);
        step.setExperiment(this);
    }
    
    /**
     * Remove an experiment step
     */
    public void removeStep(ExperimentStep step) {
        steps.remove(step);
        step.setExperiment(null);
    }
}
