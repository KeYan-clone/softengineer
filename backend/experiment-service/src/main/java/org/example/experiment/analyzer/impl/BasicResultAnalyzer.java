package org.example.experiment.analyzer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.experiment.analyzer.ResultAnalyzer;
import org.example.experiment.dto.ExperimentResultDTO;
import org.example.experiment.model.ResultStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic implementation of ResultAnalyzer
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BasicResultAnalyzer implements ResultAnalyzer {
    
    private final ObjectMapper objectMapper;
    
    @Override
    public Map<String, Object> analyzeResult(ExperimentResultDTO resultDTO) {
        log.debug("Analyzing result with execution id: {}", resultDTO.getExecutionId());
        
        Map<String, Object> analysis = new HashMap<>();
        
        // Add basic metadata
        analysis.put("executionId", resultDTO.getExecutionId());
        analysis.put("experimentId", resultDTO.getExperimentId());
        analysis.put("status", resultDTO.getStatus());
        
        // Handle different result statuses
        if (resultDTO.getStatus() == ResultStatus.COMPLETED) {
            // Add execution time
            if (resultDTO.getStartTime() != null && resultDTO.getEndTime() != null) {
                long executionTimeMillis = java.time.Duration.between(
                        resultDTO.getStartTime(), resultDTO.getEndTime()).toMillis();
                analysis.put("executionTimeMs", executionTimeMillis);
            }
            
            // Parse and analyze result data
            if (resultDTO.getResultData() != null && !resultDTO.getResultData().isEmpty()) {
                try {
                    JsonNode resultNode = objectMapper.readTree(resultDTO.getResultData());
                    
                    // Extract metrics from the result data
                    if (resultNode.has("metrics")) {
                        JsonNode metricsNode = resultNode.get("metrics");
                        Map<String, Object> metrics = new HashMap<>();
                        
                        metricsNode.fields().forEachRemaining(entry -> 
                            metrics.put(entry.getKey(), parseJsonValue(entry.getValue())));
                        
                        analysis.put("metrics", metrics);
                    }
                    
                    // Calculate success rate from step results
                    if (resultNode.has("stepResults")) {
                        JsonNode stepsNode = resultNode.get("stepResults");
                        int totalSteps = stepsNode.size();
                        int successfulSteps = 0;
                        
                        for (JsonNode stepNode : stepsNode) {
                            if (stepNode.has("status") && 
                                "SUCCESS".equalsIgnoreCase(stepNode.get("status").asText())) {
                                successfulSteps++;
                            }
                        }
                        
                        double successRate = totalSteps > 0 ? 
                            (double) successfulSteps / totalSteps * 100 : 0;
                        analysis.put("successRate", successRate);
                    }
                    
                } catch (JsonProcessingException e) {
                    log.error("Error parsing result data: {}", e.getMessage());
                    analysis.put("parseError", "Invalid result data format");
                }
            }
        } else if (resultDTO.getStatus() == ResultStatus.FAILED) {
            analysis.put("error", resultDTO.getErrorMessage());
        }
        
        return analysis;
    }
    
    @Override
    public String generateSummary(ExperimentResultDTO resultDTO) {
        log.debug("Generating summary for result with execution id: {}", resultDTO.getExecutionId());
        
        StringBuilder summary = new StringBuilder();
        
        // Add execution ID and status
        summary.append("Execution ID: ").append(resultDTO.getExecutionId()).append("\n");
        summary.append("Status: ").append(resultDTO.getStatus()).append("\n");
        
        // Add timing information if available
        if (resultDTO.getStartTime() != null) {
            summary.append("Started: ").append(resultDTO.getStartTime()).append("\n");
            
            if (resultDTO.getEndTime() != null) {
                summary.append("Completed: ").append(resultDTO.getEndTime()).append("\n");
                
                long executionTimeMillis = java.time.Duration.between(
                        resultDTO.getStartTime(), resultDTO.getEndTime()).toMillis();
                summary.append("Execution Time: ").append(executionTimeMillis).append(" ms\n");
            }
        }
        
        // Add result summary for completed executions
        if (resultDTO.getStatus() == ResultStatus.COMPLETED && 
                resultDTO.getResultData() != null && !resultDTO.getResultData().isEmpty()) {
            try {
                JsonNode resultNode = objectMapper.readTree(resultDTO.getResultData());
                
                // Add execution summary if available
                if (resultNode.has("executionSummary")) {
                    JsonNode summaryNode = resultNode.get("executionSummary");
                    
                    summary.append("\nExecution Summary:\n");
                    summaryNode.fields().forEachRemaining(entry -> 
                        summary.append("- ").append(entry.getKey()).append(": ")
                               .append(entry.getValue().asText()).append("\n"));
                }
                
                // Add metrics if available
                if (resultNode.has("metrics")) {
                    JsonNode metricsNode = resultNode.get("metrics");
                    
                    summary.append("\nMetrics:\n");
                    metricsNode.fields().forEachRemaining(entry -> 
                        summary.append("- ").append(entry.getKey()).append(": ")
                               .append(entry.getValue().asText()).append("\n"));
                }
                
            } catch (JsonProcessingException e) {
                log.error("Error parsing result data: {}", e.getMessage());
                summary.append("\nError: Unable to parse result data.\n");
            }
        } else if (resultDTO.getStatus() == ResultStatus.FAILED) {
            // Add error message for failed executions
            summary.append("\nError: ").append(resultDTO.getErrorMessage()).append("\n");
        }
        
        return summary.toString();
    }
    
    /**
     * Parse a JsonNode to the appropriate Java type
     *
     * @param node JsonNode to parse
     * @return Object representing the JsonNode value
     */
    private Object parseJsonValue(JsonNode node) {
        if (node.isTextual()) {
            return node.asText();
        } else if (node.isNumber()) {
            return node.isInt() ? node.asInt() : 
                   node.isLong() ? node.asLong() : 
                   node.asDouble();
        } else if (node.isBoolean()) {
            return node.asBoolean();
        } else {
            return node.toString();
        }
    }
}
