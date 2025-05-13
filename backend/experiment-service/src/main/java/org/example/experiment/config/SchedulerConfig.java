package org.example.experiment.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.experiment.dto.ExperimentResultDTO;
import org.example.experiment.model.ResultStatus;
import org.example.experiment.service.ExperimentResultService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduler configuration for handling background tasks
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final ExperimentResultService resultService;
    
    /**
     * Monitor running experiments for timeout
     * Runs every 5 minutes
     */
    @Scheduled(fixedRate = 300000)
    public void monitorRunningExperiments() {
        log.debug("Checking for stuck experiment executions");
        
        // Get all running executions
        List<ExperimentResultDTO> runningResults = resultService.getResultsByStatus(ResultStatus.RUNNING);
        
        LocalDateTime now = LocalDateTime.now();
        
        // Check for executions running for more than 30 minutes (configurable)
        Duration timeoutThreshold = Duration.ofMinutes(30);
        
        for (ExperimentResultDTO result : runningResults) {
            if (result.getStartTime() != null) {
                Duration runningDuration = Duration.between(result.getStartTime(), now);
                
                if (runningDuration.compareTo(timeoutThreshold) > 0) {
                    log.warn("Experiment execution with ID {} has been running for {} minutes, marking as timed out",
                            result.getExecutionId(), runningDuration.toMinutes());
                    
                    // Mark as timed out
                    resultService.updateResultStatus(result.getExecutionId(), ResultStatus.TIMEOUT);
                }
            }
        }
    }
}
