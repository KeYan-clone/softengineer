package org.example.experiment.exception;

/**
 * Exception thrown when a result is not found
 */
public class ResultNotFoundException extends RuntimeException {
    public ResultNotFoundException(String message) {
        super(message);
    }
}
