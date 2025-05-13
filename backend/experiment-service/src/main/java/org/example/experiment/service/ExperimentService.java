package org.example.experiment.service;

import org.example.experiment.dto.ExperimentCreateDTO;
import org.example.experiment.dto.ExperimentDTO;
import org.example.experiment.model.ExperimentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service interface for experiment operations
 */
public interface ExperimentService {
    
    /**
     * Create a new experiment
     * 
     * @param createDTO DTO containing experiment creation data
     * @param userId User ID of the creator
     * @return Created experiment DTO
     */
    ExperimentDTO createExperiment(ExperimentCreateDTO createDTO, String userId);
    
    /**
     * Update an existing experiment
     * 
     * @param id Experiment ID
     * @param updateDTO DTO containing experiment update data
     * @return Updated experiment DTO
     */
    ExperimentDTO updateExperiment(Long id, ExperimentCreateDTO updateDTO);
    
    /**
     * Get experiment by ID
     * 
     * @param id Experiment ID
     * @return Optional containing the experiment DTO if found
     */
    Optional<ExperimentDTO> getExperimentById(Long id);
    
    /**
     * Get experiment by ID and user ID
     * 
     * @param id Experiment ID
     * @param userId User ID
     * @return Optional containing the experiment DTO if found
     */
    Optional<ExperimentDTO> getExperimentByIdAndUserId(Long id, String userId);
    
    /**
     * Get all experiments with pagination
     * 
     * @param pageable Pagination information
     * @return Page of experiment DTOs
     */
    Page<ExperimentDTO> getAllExperiments(Pageable pageable);
    
    /**
     * Get experiments by user ID with pagination
     * 
     * @param userId User ID
     * @param pageable Pagination information
     * @return Page of experiment DTOs
     */
    Page<ExperimentDTO> getExperimentsByUserId(String userId, Pageable pageable);
    
    /**
     * Get experiments by status with pagination
     * 
     * @param status Experiment status
     * @param pageable Pagination information
     * @return Page of experiment DTOs
     */
    Page<ExperimentDTO> getExperimentsByStatus(ExperimentStatus status, Pageable pageable);
    
    /**
     * Get experiments by user ID and status with pagination
     * 
     * @param userId User ID
     * @param status Experiment status
     * @param pageable Pagination information
     * @return Page of experiment DTOs
     */
    Page<ExperimentDTO> getExperimentsByUserIdAndStatus(String userId, ExperimentStatus status, Pageable pageable);
    
    /**
     * Search experiments by name with pagination
     * 
     * @param name Name to search for
     * @param pageable Pagination information
     * @return Page of experiment DTOs
     */
    Page<ExperimentDTO> searchExperimentsByName(String name, Pageable pageable);
    
    /**
     * Update experiment status
     * 
     * @param id Experiment ID
     * @param status New status
     * @return Updated experiment DTO
     */
    ExperimentDTO updateExperimentStatus(Long id, ExperimentStatus status);
    
    /**
     * Delete experiment by ID
     * 
     * @param id Experiment ID
     * @return True if deleted successfully, false otherwise
     */
    boolean deleteExperiment(Long id);
    
    /**
     * Share experiment (set status to PUBLISHED)
     * 
     * @param id Experiment ID
     * @return Updated experiment DTO
     */
    ExperimentDTO shareExperiment(Long id);
    
    /**
     * Get published experiments with pagination
     * 
     * @param pageable Pagination information
     * @return Page of experiment DTOs
     */
    Page<ExperimentDTO> getPublishedExperiments(Pageable pageable);
}
