package org.example.experiment.exception;

/**
 * Exception thrown when an experiment is not found
 */
public class ExperimentNotFoundException extends RuntimeException {
    public ExperimentNotFoundException(String message) {
        super(message);
    }
}
