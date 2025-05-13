package org.example.experiment.service;

import org.example.experiment.dto.ExperimentResultDTO;
import org.example.experiment.model.ResultStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for experiment result operations
 */
public interface ExperimentResultService {
    
    /**
     * Create a new experiment result
     * 
     * @param experimentId Experiment ID
     * @return Created experiment result DTO
     */
    ExperimentResultDTO createExperimentResult(Long experimentId);
    
    /**
     * Get experiment result by ID
     * 
     * @param id Result ID
     * @return Optional containing the result DTO if found
     */
    Optional<ExperimentResultDTO> getResultById(Long id);
    
    /**
     * Get experiment result by execution ID
     * 
     * @param executionId Execution ID
     * @return Optional containing the result DTO if found
     */
    Optional<ExperimentResultDTO> getResultByExecutionId(String executionId);
    
    /**
     * Get results for an experiment with pagination
     * 
     * @param experimentId Experiment ID
     * @param pageable Pagination information
     * @return Page of result DTOs
     */
    Page<ExperimentResultDTO> getResultsByExperimentId(Long experimentId, Pageable pageable);
    
    /**
     * Update result status
     * 
     * @param executionId Execution ID
     * @param status New status
     * @return Updated result DTO
     */
    ExperimentResultDTO updateResultStatus(String executionId, ResultStatus status);
    
    /**
     * Save experiment result data
     * 
     * @param executionId Execution ID
     * @param resultData Result data as JSON string
     * @return Updated result DTO
     */
    ExperimentResultDTO saveResultData(String executionId, String resultData);
    
    /**
     * Get results by status
     * 
     * @param status Result status
     * @return List of result DTOs
     */
    List<ExperimentResultDTO> getResultsByStatus(ResultStatus status);
    
    /**
     * Record execution failure
     * 
     * @param executionId Execution ID
     * @param errorMessage Error message
     * @return Updated result DTO
     */
    ExperimentResultDTO recordFailure(String executionId, String errorMessage);
}
