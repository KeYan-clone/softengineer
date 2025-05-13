package org.example.backend.core.repository;

import org.example.backend.core.domain.Evaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Evaluation repository interface
 */
public interface EvaluationRepository extends JpaRepository<Evaluation, String> {
    
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
     * Calculate average rating for an experiment
     * 
     * @param experimentId experiment ID
     * @return average rating
     */
    @Query("SELECT AVG(e.rating) FROM Evaluation e WHERE e.experimentId = :experimentId")
    Double calculateAverageRatingByExperimentId(@Param("experimentId") String experimentId);
    
    /**
     * Count evaluations by experiment ID
     * 
     * @param experimentId experiment ID
     * @return count
     */
    long countByExperimentId(String experimentId);
}
