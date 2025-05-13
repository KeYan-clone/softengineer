package org.example.experiment.executor.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.experiment.dto.ExperimentDTO;
import org.example.experiment.dto.ExperimentResultDTO;
import org.example.experiment.exception.ExperimentNotFoundException;
import org.example.experiment.executor.ExperimentExecutor;
import org.example.experiment.model.ExperimentStatus;
import org.example.experiment.model.ResultStatus;
import org.example.experiment.service.ExperimentResultService;
import org.example.experiment.service.ExperimentService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Basic implementation of ExperimentExecutor
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BasicExperimentExecutor implements ExperimentExecutor {
    
    private final ExperimentService experimentService;
    private final ExperimentResultService resultService;
    
    // Map to track running executions
    private final Map<String, CompletableFuture<Void>> runningExecutions = new ConcurrentHashMap<>();
    
    @Override
    public String executeExperiment(Long experimentId) {
        log.info("Executing experiment with id: {}", experimentId);
        
        // Get experiment details
        Optional<ExperimentDTO> experimentOpt = experimentService.getExperimentById(experimentId);
        if (experimentOpt.isEmpty()) {
            throw new ExperimentNotFoundException("Experiment not found with id: " + experimentId);
        }
        
        // Update experiment status to RUNNING
        experimentService.updateExperimentStatus(experimentId, ExperimentStatus.RUNNING);
        
        // Create a new result record
        ExperimentResultDTO result = resultService.createExperimentResult(experimentId);
        String executionId = result.getExecutionId();
        
        // Start execution asynchronously
        runExperiment(experimentId, executionId);
        
        return executionId;
    }
    
    @Override
    public boolean cancelExecution(String executionId) {
        log.info("Cancelling execution with id: {}", executionId);
        
        CompletableFuture<Void> execution = runningExecutions.get(executionId);
        if (execution != null && !execution.isDone()) {
            execution.cancel(true);
            runningExecutions.remove(executionId);
            
            // Update result status
            resultService.updateResultStatus(executionId, ResultStatus.CANCELLED);
            return true;
        }
        
        return false;
    }
    
    /**
     * Run experiment asynchronously
     * 
     * @param experimentId Experiment ID
     * @param executionId Execution ID
     */
    @Async
    protected void runExperiment(Long experimentId, String executionId) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                log.debug("Started execution of experiment: {} with execution id: {}", experimentId, executionId);
                
                // Update result status to RUNNING
                resultService.updateResultStatus(executionId, ResultStatus.RUNNING);
                
                // Simulate experiment execution
                executeExperimentLogic(experimentId, executionId);
                
                // Update experiment status to COMPLETED
                experimentService.updateExperimentStatus(experimentId, ExperimentStatus.COMPLETED);
                
                log.debug("Completed execution of experiment: {}", experimentId);
            } catch (InterruptedException e) {
                log.info("Experiment execution was interrupted: {}", experimentId);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("Error executing experiment: " + experimentId, e);
                
                // Record failure
                resultService.recordFailure(executionId, e.getMessage());
                
                // Update experiment status to FAILED
                experimentService.updateExperimentStatus(experimentId, ExperimentStatus.FAILED);
            } finally {
                runningExecutions.remove(executionId);
            }
        });
        
        runningExecutions.put(executionId, future);
    }
    
    /**
     * Execute the actual experiment logic
     * 
     * @param experimentId Experiment ID
     * @param executionId Execution ID
     * @throws Exception If execution fails
     * @throws InterruptedException If execution is interrupted
     */
    private void executeExperimentLogic(Long experimentId, String executionId) 
            throws Exception, InterruptedException {
        // This is a placeholder for actual experiment execution logic
        // In a real implementation, this would execute the steps of the experiment
        
        log.info("Executing experiment {} with execution ID {}", experimentId, executionId);
        
        // Simulate processing time
        Thread.sleep(5000);
        
        // Simulate generating results
        String resultData = generateSampleResultData();
        
        // Save result data
        resultService.saveResultData(executionId, resultData);
    }
    
    /**
     * Generate sample result data for demonstration
     * 
     * @return JSON string with sample results
     */
    private String generateSampleResultData() {
        return """
                {
                    "executionSummary": {
                        "totalSteps": 3,
                        "completedSteps": 3,
                        "successRate": 100
                    },
                    "metrics": {
                        "executionTime": 4832,
                        "cpuUsage": 45.7,
                        "memoryUsage": 128.5
                    },
                    "stepResults": [
                        {
                            "stepId": 1,
                            "status": "SUCCESS",
                            "outputData": {
                                "type": "dataset",
                                "rows": 150,
                                "columns": 4
                            }
                        },
                        {
                            "stepId": 2,
                            "status": "SUCCESS",
                            "outputData": {
                                "type": "model",
                                "accuracy": 0.95,
                                "parameters": {
                                    "alpha": 0.01,
                                    "iterations": 100
                                }
                            }
                        },
                        {
                            "stepId": 3,
                            "status": "SUCCESS",
                            "outputData": {
                                "type": "visualization",
                                "chartType": "scatter",
                                "dataPoints": 150
                            }
                        }
                    ]
                }
                """;
    }
}
