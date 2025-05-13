package org.example.experiment.analyzer;

import org.example.experiment.dto.ExperimentResultDTO;

import java.util.Map;

/**
 * Interface for experiment result analysis
 */
public interface ResultAnalyzer {
    
    /**
     * Analyze experiment results
     * 
     * @param resultDTO Result data to analyze
     * @return Map of analysis metrics
     */
    Map<String, Object> analyzeResult(ExperimentResultDTO resultDTO);
    
    /**
     * Generate result summary
     * 
     * @param resultDTO Result data to summarize
     * @return Summary as a string
     */
    String generateSummary(ExperimentResultDTO resultDTO);
}
