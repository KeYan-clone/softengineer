package org.example.backend.core.event;

/**
 * Domain event interface 
 * Base interface for all domain events
 */
public interface DomainEvent {
    
    /**
     * Get event ID
     * 
     * @return event ID
     */
    String getEventId();
    
    /**
     * Get event timestamp
     * 
     * @return event timestamp in milliseconds
     */
    long getTimestamp();
    
    /**
     * Get event type
     * 
     * @return event type name
     */
    String getEventType();
}
