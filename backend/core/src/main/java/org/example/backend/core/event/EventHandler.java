package org.example.backend.core.event;

/**
 * Domain event handler interface
 * Defines operations for handling domain events
 * 
 * @param <T> the type of domain event to handle
 */
public interface EventHandler<T extends DomainEvent> {
    
    /**
     * Handle a domain event
     * 
     * @param event the domain event to handle
     */
    void handle(T event);
    
    /**
     * Get the event type that this handler can handle
     * 
     * @return the event class
     */
    Class<T> getEventType();
}
