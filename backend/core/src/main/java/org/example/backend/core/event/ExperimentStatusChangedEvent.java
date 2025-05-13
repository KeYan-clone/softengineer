package org.example.backend.core.event;

import lombok.Getter;
import org.example.backend.core.domain.ExperimentStatus;

/**
 * Event fired when an experiment status changes
 */
@Getter
public class ExperimentStatusChangedEvent extends ExperimentEvent {
    
    /**
     * Old status
     */
    private final ExperimentStatus oldStatus;
    
    /**
     * New status
     */
    private final ExperimentStatus newStatus;
    
    /**
     * Constructor
     * 
     * @param experimentId experiment ID
     * @param oldStatus old status
     * @param newStatus new status
     */
    public ExperimentStatusChangedEvent(String experimentId, 
                                        ExperimentStatus oldStatus, 
                                        ExperimentStatus newStatus) {
        super(experimentId);
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
}
