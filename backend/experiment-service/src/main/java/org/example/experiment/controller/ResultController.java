package org.example.experiment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.experiment.analyzer.ResultAnalyzer;
import org.example.experiment.dto.ApiResponse;
import org.example.experiment.dto.ExperimentResultDTO;
import org.example.experiment.exception.ExperimentNotFoundException;
import org.example.experiment.exception.ResultNotFoundException;
import org.example.experiment.executor.ExperimentExecutor;
import org.example.experiment.service.ExperimentResultService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for experiment result operations
 */
@RestController
@RequestMapping("/api/experiments")
@RequiredArgsConstructor
@Tag(name = "Experiment Result API", description = "API endpoints for experiment execution and results")
@Slf4j
public class ResultController {

    private final ExperimentExecutor experimentExecutor;
    private final ExperimentResultService resultService;
    private final ResultAnalyzer resultAnalyzer;

    /**
     * Run an experiment
     *
     * @param experimentId Experiment ID
     * @return Execution ID
     */
    @PostMapping("/{experimentId}/run")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Run an experiment", description = "Start the execution of an experiment")
    public ApiResponse<String> runExperiment(
            @Parameter(description = "Experiment ID") @PathVariable Long experimentId) {
        log.info("Running experiment with ID: {}", experimentId);
        
        String executionId = experimentExecutor.executeExperiment(experimentId);
        
        return ApiResponse.success(executionId);
    }
    
    /**
     * Cancel an experiment execution
     *
     * @param executionId Execution ID
     * @return Success response
     */
    @DeleteMapping("/executions/{executionId}")
    @Operation(summary = "Cancel experiment execution", 
               description = "Cancel a running experiment execution")
    public ApiResponse<Boolean> cancelExecution(
            @Parameter(description = "Execution ID") @PathVariable String executionId) {
        log.info("Cancelling execution with ID: {}", executionId);
        
        boolean cancelled = experimentExecutor.cancelExecution(executionId);
        
        return ApiResponse.success(cancelled);
    }
    
    /**
     * Get experiment results
     *
     * @param experimentId Experiment ID
     * @param page Page number
     * @param size Page size
     * @return Page of results
     */
    @GetMapping("/{experimentId}/results")
    @Operation(summary = "Get experiment results", 
               description = "Get paginated list of results for an experiment")
    public ApiResponse<Page<ExperimentResultDTO>> getResults(
            @Parameter(description = "Experiment ID") @PathVariable Long experimentId,
            
            @Parameter(description = "Page number (zero-based)") 
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size") 
            @RequestParam(defaultValue = "10") int size) {
        log.info("Getting results for experiment with ID: {}, page: {}, size: {}", 
                  experimentId, page, size);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());
        Page<ExperimentResultDTO> results = resultService.getResultsByExperimentId(experimentId, pageable);
        
        return ApiResponse.success(results);
    }
    
    /**
     * Get result by ID
     *
     * @param id Result ID
     * @return Result data
     */
    @GetMapping("/results/{id}")
    @Operation(summary = "Get result by ID", description = "Get detailed information about a result")
    public ApiResponse<ExperimentResultDTO> getResult(
            @Parameter(description = "Result ID") @PathVariable Long id) {
        log.info("Getting result with ID: {}", id);
        
        ExperimentResultDTO result = resultService.getResultById(id)
                .orElseThrow(() -> new ResultNotFoundException("Result not found with id: " + id));
        
        return ApiResponse.success(result);
    }
    
    /**
     * Get result by execution ID
     *
     * @param executionId Execution ID
     * @return Result data
     */
    @GetMapping("/executions/{executionId}")
    @Operation(summary = "Get result by execution ID", 
               description = "Get detailed information about a result by execution ID")
    public ApiResponse<ExperimentResultDTO> getResultByExecutionId(
            @Parameter(description = "Execution ID") @PathVariable String executionId) {
        log.info("Getting result with execution ID: {}", executionId);
        
        ExperimentResultDTO result = resultService.getResultByExecutionId(executionId)
                .orElseThrow(() -> new ResultNotFoundException(
                        "Result not found with execution id: " + executionId));
        
        return ApiResponse.success(result);
    }
    
    /**
     * Analyze result
     *
     * @param id Result ID
     * @return Analysis data
     */
    @GetMapping("/results/{id}/analysis")
    @Operation(summary = "Analyze result", description = "Get analysis of experiment result")
    public ApiResponse<Map<String, Object>> analyzeResult(
            @Parameter(description = "Result ID") @PathVariable Long id) {
        log.info("Analyzing result with ID: {}", id);
        
        ExperimentResultDTO result = resultService.getResultById(id)
                .orElseThrow(() -> new ResultNotFoundException("Result not found with id: " + id));
        
        Map<String, Object> analysis = resultAnalyzer.analyzeResult(result);
        
        return ApiResponse.success(analysis);
    }
    
    /**
     * Get result summary
     *
     * @param id Result ID
     * @return Summary text
     */
    @GetMapping("/results/{id}/summary")
    @Operation(summary = "Get result summary", description = "Get summary of experiment result")
    public ApiResponse<String> getResultSummary(
            @Parameter(description = "Result ID") @PathVariable Long id) {
        log.info("Getting summary for result with ID: {}", id);
        
        ExperimentResultDTO result = resultService.getResultById(id)
                .orElseThrow(() -> new ResultNotFoundException("Result not found with id: " + id));
        
        String summary = resultAnalyzer.generateSummary(result);
        
        return ApiResponse.success(summary);
    }
}
