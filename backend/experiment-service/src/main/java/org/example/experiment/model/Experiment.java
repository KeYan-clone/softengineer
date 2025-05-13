package org.example.experiment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Experiment entity representing an experiment in the system
 */
@Entity
@Table(name = "experiments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Experiment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 2000)
    private String description;
    
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExperimentStatus status;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "experiment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExperimentStep> steps = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = ExperimentStatus.DRAFT;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Add a step to the experiment
     * 
     * @param step The step to add
     * @return The updated experiment
     */
    public Experiment addStep(ExperimentStep step) {
        steps.add(step);
        step.setExperiment(this);
        return this;
    }
    
    /**
     * Remove a step from the experiment
     * 
     * @param step The step to remove
     * @return The updated experiment
     */
    public Experiment removeStep(ExperimentStep step) {
        steps.remove(step);
        step.setExperiment(null);
        return this;
    }
}