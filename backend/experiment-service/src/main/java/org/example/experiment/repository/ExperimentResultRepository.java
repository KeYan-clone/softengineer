package org.example.experiment.repository;

import org.example.experiment.model.ExperimentResult;
import org.example.experiment.model.ResultStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for ExperimentResult entity
 */
@Repository
public interface ExperimentResultRepository extends JpaRepository<ExperimentResult, Long> {
    
    /**
     * Find all results for an experiment
     * 
     * @param experimentId Experiment ID
     * @param pageable Pagination information
     * @return Page of experiment results
     */
    Page<ExperimentResult> findByExperimentId(Long experimentId, Pageable pageable);
    
    /**
     * Find a result by execution ID
     * 
     * @param executionId Execution ID
     * @return Optional of ExperimentResult
     */
    Optional<ExperimentResult> findByExecutionId(String executionId);
    
    /**
     * Find results by status
     * 
     * @param status Result status
     * @return List of experiment results
     */
    List<ExperimentResult> findByStatus(ResultStatus status);
    
    /**
     * Find results by experiment ID and status
     * 
     * @param experimentId Experiment ID
     * @param status Result status
     * @return List of experiment results
     */
    List<ExperimentResult> findByExperimentIdAndStatus(Long experimentId, ResultStatus status);
}
