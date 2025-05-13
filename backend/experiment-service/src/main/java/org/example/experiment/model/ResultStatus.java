package org.example.experiment.model;

/**
 * Enum representing the status of experiment results
 */
public enum ResultStatus {
    PENDING,        // Execution is pending
    RUNNING,        // Execution is running
    COMPLETED,      // Execution completed successfully
    FAILED,         // Execution failed
    TIMEOUT,        // Execution timed out
    CANCELLED       // Execution was cancelled
}
