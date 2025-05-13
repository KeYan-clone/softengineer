package org.example.experiment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.experiment.dto.ApiResponse;
import org.example.experiment.dto.ExperimentCreateDTO;
import org.example.experiment.dto.ExperimentDTO;
import org.example.experiment.exception.ExperimentNotFoundException;
import org.example.experiment.model.ExperimentStatus;
import org.example.experiment.service.ExperimentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for experiment operations
 */
@RestController
@RequestMapping("/api/experiments")
@RequiredArgsConstructor
@Tag(name = "Experiment API", description = "API endpoints for experiment management")
@Slf4j
public class ExperimentController {
    
    private final ExperimentService experimentService;
    
    /**
     * Create a new experiment
     *
     * @param createDTO Experiment creation data
     * @return Created experiment
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new experiment", description = "Create a new experiment with steps")
    public ApiResponse<ExperimentDTO> createExperiment(@Valid @RequestBody ExperimentCreateDTO createDTO) {
        log.info("Creating new experiment: {}", createDTO.getName());
        
        String userId = getCurrentUserId();
        ExperimentDTO createdExperiment = experimentService.createExperiment(createDTO, userId);
        
        return ApiResponse.success(createdExperiment);
    }
    
    /**
     * Get experiment by ID
     *
     * @param id Experiment ID
     * @return Experiment data
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get experiment by ID", description = "Get detailed information about an experiment")
    public ApiResponse<ExperimentDTO> getExperiment(
            @Parameter(description = "Experiment ID") @PathVariable Long id) {
        log.info("Getting experiment with ID: {}", id);
        
        ExperimentDTO experiment = experimentService.getExperimentById(id)
                .orElseThrow(() -> new ExperimentNotFoundException("Experiment not found with id: " + id));
        
        return ApiResponse.success(experiment);
    }
    
    /**
     * Update an experiment
     *
     * @param id Experiment ID
     * @param updateDTO Experiment update data
     * @return Updated experiment
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an experiment", description = "Update an existing experiment's details and steps")
    public ApiResponse<ExperimentDTO> updateExperiment(
            @Parameter(description = "Experiment ID") @PathVariable Long id,
            @Valid @RequestBody ExperimentCreateDTO updateDTO) {
        log.info("Updating experiment with ID: {}", id);
        
        ExperimentDTO updatedExperiment = experimentService.updateExperiment(id, updateDTO);
        
        return ApiResponse.success(updatedExperiment);
    }
    
    /**
     * Delete an experiment
     *
     * @param id Experiment ID
     * @return Success response
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an experiment", description = "Delete an experiment by ID")
    public ApiResponse<Void> deleteExperiment(
            @Parameter(description = "Experiment ID") @PathVariable Long id) {
        log.info("Deleting experiment with ID: {}", id);
        
        boolean deleted = experimentService.deleteExperiment(id);
        if (!deleted) {
            throw new ExperimentNotFoundException("Experiment not found with id: " + id);
        }
        
        return ApiResponse.success();
    }
    
    /**
     * Get current user's experiments
     *
     * @param page Page number
     * @param size Page size
     * @param status Optional status filter
     * @return Page of experiments
     */
    @GetMapping("/my")
    @Operation(summary = "Get current user's experiments", 
              description = "Get paginated list of experiments for the current user")
    public ApiResponse<Page<ExperimentDTO>> getMyExperiments(
            @Parameter(description = "Page number (zero-based)") 
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size") 
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Filter by experiment status") 
            @RequestParam(required = false) ExperimentStatus status) {
        log.info("Getting experiments for current user, page: {}, size: {}, status: {}", page, size, status);
        
        String userId = getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        
        Page<ExperimentDTO> experiments;
        if (status != null) {
            experiments = experimentService.getExperimentsByUserIdAndStatus(userId, status, pageable);
        } else {
            experiments = experimentService.getExperimentsByUserId(userId, pageable);
        }
        
        return ApiResponse.success(experiments);
    }
    
    /**
     * Search experiments
     *
     * @param query Search query
     * @param page Page number
     * @param size Page size
     * @return Page of experiments
     */
    @GetMapping("/search")
    @Operation(summary = "Search experiments", description = "Search experiments by name")
    public ApiResponse<Page<ExperimentDTO>> searchExperiments(
            @Parameter(description = "Search query") 
            @RequestParam String query,
            
            @Parameter(description = "Page number (zero-based)") 
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("Searching experiments with query: {}, page: {}, size: {}", query, page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ExperimentDTO> experiments = experimentService.searchExperimentsByName(query, pageable);
        
        return ApiResponse.success(experiments);
    }
    
    /**
     * Share an experiment (set status to PUBLISHED)
     *
     * @param id Experiment ID
     * @return Updated experiment
     */
    @PostMapping("/{id}/share")
    @Operation(summary = "Share an experiment", description = "Make an experiment publicly available")
    public ApiResponse<ExperimentDTO> shareExperiment(
            @Parameter(description = "Experiment ID") @PathVariable Long id) {
        log.info("Sharing experiment with ID: {}", id);
        
        ExperimentDTO sharedExperiment = experimentService.shareExperiment(id);
        
        return ApiResponse.success(sharedExperiment);
    }
    
    /**
     * Get published experiments
     *
     * @param page Page number
     * @param size Page size
     * @return Page of published experiments
     */
    @GetMapping("/published")
    @Operation(summary = "Get published experiments", 
              description = "Get paginated list of published experiments")
    public ApiResponse<Page<ExperimentDTO>> getPublishedExperiments(
            @Parameter(description = "Page number (zero-based)") 
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("Getting published experiments, page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        Page<ExperimentDTO> experiments = experimentService.getPublishedExperiments(pageable);
        
        return ApiResponse.success(experiments);
    }
    
    /**
     * Get current user ID from authentication context
     *
     * @return User ID
     */
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // In a real application, extract the user ID from the authentication object
        // For now, return a default user ID for testing
        return "test-user-id";
    }
}
