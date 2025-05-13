package org.example.experiment.model;

/**
 * Enum representing the status of an experiment
 */
public enum ExperimentStatus {
    DRAFT,          // Experiment is in draft mode, not yet published
    PUBLISHED,      // Experiment is published and visible to others
    RUNNING,        // Experiment is currently running
    COMPLETED,      // Experiment has completed successfully  
    FAILED,         // Experiment has failed
    ARCHIVED        // Experiment is archived
}