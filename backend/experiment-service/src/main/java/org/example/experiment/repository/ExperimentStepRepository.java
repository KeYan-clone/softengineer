package org.example.experiment.repository;

import org.example.experiment.model.ExperimentStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for ExperimentStep entity
 */
@Repository
public interface ExperimentStepRepository extends JpaRepository<ExperimentStep, Long> {
    
    /**
     * Find all steps for an experiment ordered by sequence
     * 
     * @param experimentId Experiment ID
     * @return List of experiment steps
     */
    List<ExperimentStep> findByExperimentIdOrderBySequenceAsc(Long experimentId);
    
    /**
     * Delete all steps for an experiment
     * 
     * @param experimentId Experiment ID
     */
    void deleteByExperimentId(Long experimentId);
}
