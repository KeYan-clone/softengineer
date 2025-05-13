package org.example.backend.core.service;

import org.example.backend.core.domain.Experiment;
import org.example.backend.core.domain.ExperimentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Experiment service interface
 * Defines core business operations related to experiments
 */
public interface ExperimentService {
    
    /**
     * Create a new experiment
     * 
     * @param experiment experiment to create
     * @return created experiment
     */
    Experiment createExperiment(Experiment experiment);
    
    /**
     * Find experiment by ID
     * 
     * @param id experiment ID
     * @return experiment optional
     */
    Optional<Experiment> findById(String id);
    
    /**
     * Find experiments by creator ID
     * 
     * @param userId creator ID
     * @return list of experiments
     */
    List<Experiment> findByUserId(String userId);
    
    /**
     * Find experiments by creator ID with pagination
     * 
     * @param userId creator ID
     * @param pageable pagination info
     * @return page of experiments
     */
    Page<Experiment> findByUserId(String userId, Pageable pageable);
    
    /**
     * Find experiments by status
     * 
     * @param status experiment status
     * @return list of experiments
     */
    List<Experiment> findByStatus(ExperimentStatus status);
    
    /**
     * Find experiments by status with pagination
     * 
     * @param status experiment status
     * @param pageable pagination info
     * @return page of experiments
     */
    Page<Experiment> findByStatus(ExperimentStatus status, Pageable pageable);
    
    /**
     * Update experiment
     * 
     * @param experiment experiment to update
     * @return updated experiment
     */
    Experiment updateExperiment(Experiment experiment);
    
    /**
     * Delete experiment by ID
     * 
     * @param id experiment ID
     */
    void deleteExperiment(String id);
    
    /**
     * Change experiment status
     * 
     * @param id experiment ID
     * @param status new status
     * @return updated experiment
     */
    Experiment changeStatus(String id, ExperimentStatus status);
    
    /**
     * Search experiments by name
     * 
     * @param name name to search
     * @param pageable pagination info
     * @return page of experiments
     */
    Page<Experiment> searchByName(String name, Pageable pageable);
}
