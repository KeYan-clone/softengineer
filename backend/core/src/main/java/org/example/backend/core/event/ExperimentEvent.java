package org.example.backend.core.event;

import lombok.Getter;

/**
 * Base event for experiment-related events
 */
@Getter
public abstract class ExperimentEvent extends AbstractDomainEvent {
    
    /**
     * Experiment ID
     */
    private final String experimentId;
    
    /**
     * Constructor
     * 
     * @param experimentId experiment ID
     */
    protected ExperimentEvent(String experimentId) {
        super();
        this.experimentId = experimentId;
    }
}
