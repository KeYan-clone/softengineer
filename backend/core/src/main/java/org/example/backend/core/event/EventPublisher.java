package org.example.backend.core.event;

/**
 * Event publisher interface
 * Defines operations for publishing domain events
 */
public interface EventPublisher {
    
    /**
     * Publish a domain event
     * 
     * @param event the domain event to publish
     */
    void publish(DomainEvent event);
}
