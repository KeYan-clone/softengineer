package org.example.backend.core.event;

import lombok.Getter;

/**
 * Event fired when an experiment is created
 */
@Getter
public class ExperimentCreatedEvent extends ExperimentEvent {
    
    /**
     * Experiment name
     */
    private final String experimentName;
    
    /**
     * Creator ID
     */
    private final String creatorId;
    
    /**
     * Constructor
     * 
     * @param experimentId experiment ID
     * @param experimentName experiment name
     * @param creatorId creator ID
     */
    public ExperimentCreatedEvent(String experimentId, String experimentName, String creatorId) {
        super(experimentId);
        this.experimentName = experimentName;
        this.creatorId = creatorId;
    }
}
