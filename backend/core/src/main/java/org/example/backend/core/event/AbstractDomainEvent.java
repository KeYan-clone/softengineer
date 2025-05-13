package org.example.backend.core.event;

import lombok.Getter;
import org.example.backend.common.utils.StringUtils;

/**
 * Base domain event implementation
 * Provides common functionality for all domain events
 */
@Getter
public abstract class AbstractDomainEvent implements DomainEvent {
    
    /**
     * Event ID
     */
    private final String eventId;
    
    /**
     * Event timestamp
     */
    private final long timestamp;
    
    /**
     * Constructor
     */
    protected AbstractDomainEvent() {
        this.eventId = StringUtils.generateUuid();
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * Get event type name
     * Uses class simple name by default
     * 
     * @return event type name
     */
    @Override
    public String getEventType() {
        return this.getClass().getSimpleName();
    }
}
