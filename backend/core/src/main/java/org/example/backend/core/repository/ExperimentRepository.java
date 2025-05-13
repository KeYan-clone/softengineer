package org.example.backend.core.repository;

import org.example.backend.core.domain.Experiment;
import org.example.backend.core.domain.ExperimentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Experiment repository interface
 */
public interface ExperimentRepository extends JpaRepository<Experiment, String> {
    
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
     * Find experiments by name containing the given text
     * 
     * @param name name text to search
     * @param pageable pagination info
     * @return page of experiments
     */
    Page<Experiment> findByNameContaining(String name, Pageable pageable);
}
