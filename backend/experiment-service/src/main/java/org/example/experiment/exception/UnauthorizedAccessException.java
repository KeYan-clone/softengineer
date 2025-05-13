package org.example.experiment.exception;

/**
 * Exception thrown when a user tries to access a resource they don't have permission for
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
