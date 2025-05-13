package org.example.backend.core.service;

import org.example.backend.core.domain.Evaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Evaluation service interface
 * Defines core business operations related to evaluations
 */
public interface EvaluationService {
    
    /**
     * Create a new evaluation
     * 
     * @param evaluation evaluation to create
     * @return created evaluation
     */
    Evaluation createEvaluation(Evaluation evaluation);
    
    /**
     * Find evaluation by ID
     * 
     * @param id evaluation ID
     * @return evaluation optional
     */
    Optional<Evaluation> findById(String id);
    
    /**
     * Find evaluations by experiment ID
     * 
     * @param experimentId experiment ID
     * @return list of evaluations
     */
    List<Evaluation> findByExperimentId(String experimentId);
    
    /**
     * Find evaluations by experiment ID with pagination
     * 
     * @param experimentId experiment ID
     * @param pageable pagination info
     * @return page of evaluations
     */
    Page<Evaluation> findByExperimentId(String experimentId, Pageable pageable);
    
    /**
     * Find evaluations by user ID
     * 
     * @param userId user ID
     * @return list of evaluations
     */
    List<Evaluation> findByUserId(String userId);
    
    /**
     * Find evaluations by user ID with pagination
     * 
     * @param userId user ID
     * @param pageable pagination info
     * @return page of evaluations
     */
    Page<Evaluation> findByUserId(String userId, Pageable pageable);
    
    /**
     * Find evaluation by experiment ID and user ID
     * 
     * @param experimentId experiment ID
     * @param userId user ID
     * @return evaluation optional
     */
    Optional<Evaluation> findByExperimentIdAndUserId(String experimentId, String userId);
    
    /**
     * Update evaluation
     * 
     * @param evaluation evaluation to update
     * @return updated evaluation
     */
    Evaluation updateEvaluation(Evaluation evaluation);
    
    /**
     * Delete evaluation by ID
     * 
     * @param id evaluation ID
     */
    void deleteEvaluation(String id);
    
    /**
     * Calculate average rating for an experiment
     * 
     * @param experimentId experiment ID
     * @return average rating
     */
    Double calculateAverageRating(String experimentId);
    
    /**
     * Count evaluations by experiment ID
     * 
     * @param experimentId experiment ID
     * @return count of evaluations
     */
    long countByExperimentId(String experimentId);
}
