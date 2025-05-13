package org.example.experiment.repository;

import org.example.experiment.model.Experiment;
import org.example.experiment.model.ExperimentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Experiment entity
 */
@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, Long> {
    
    /**
     * Find all experiments by user ID
     * 
     * @param userId User ID
     * @param pageable Pagination information
     * @return Page of experiments
     */
    Page<Experiment> findByUserId(String userId, Pageable pageable);
    
    /**
     * Find all experiments by status
     * 
     * @param status Experiment status
     * @param pageable Pagination information
     * @return Page of experiments
     */
    Page<Experiment> findByStatus(ExperimentStatus status, Pageable pageable);
    
    /**
     * Find all experiments by user ID and status
     * 
     * @param userId User ID
     * @param status Experiment status
     * @param pageable Pagination information
     * @return Page of experiments
     */
    Page<Experiment> findByUserIdAndStatus(String userId, ExperimentStatus status, Pageable pageable);
    
    /**
     * Find experiments by name containing the given text
     * 
     * @param name Name text to search for
     * @param pageable Pagination information
     * @return Page of experiments
     */
    Page<Experiment> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    /**
     * Find experiments by name containing given text and user ID
     * 
     * @param name Name text to search for
     * @param userId User ID
     * @param pageable Pagination information
     * @return Page of experiments
     */
    Page<Experiment> findByNameContainingIgnoreCaseAndUserId(String name, String userId, Pageable pageable);
    
    /**
     * Find an experiment by ID and user ID
     * 
     * @param id Experiment ID
     * @param userId User ID
     * @return Optional of Experiment
     */
    Optional<Experiment> findByIdAndUserId(Long id, String userId);
    
    /**
     * Find published experiments
     * 
     * @param pageable Pagination information
     * @return Page of experiments
     */
    @Query("SELECT e FROM Experiment e WHERE e.status = 'PUBLISHED'")
    Page<Experiment> findPublishedExperiments(Pageable pageable);
}
