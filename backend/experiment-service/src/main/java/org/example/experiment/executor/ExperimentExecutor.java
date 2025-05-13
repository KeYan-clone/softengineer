package org.example.experiment.executor;

/**
 * Interface for experiment execution
 */
public interface ExperimentExecutor {
    
    /**
     * Execute an experiment
     * 
     * @param experimentId Experiment ID
     * @return Execution ID
     */
    String executeExperiment(Long experimentId);
    
    /**
     * Cancel execution
     * 
     * @param executionId Execution ID
     * @return True if cancelled successfully, false otherwise
     */
    boolean cancelExecution(String executionId);
}
