package org.example.experiment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.experiment.dto.ExperimentResultDTO;
import org.example.experiment.exception.ExperimentNotFoundException;
import org.example.experiment.exception.ResultNotFoundException;
import org.example.experiment.model.ExperimentResult;
import org.example.experiment.model.ResultStatus;
import org.example.experiment.repository.ExperimentRepository;
import org.example.experiment.repository.ExperimentResultRepository;
import org.example.experiment.service.ExperimentResultService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of ExperimentResultService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExperimentResultServiceImpl implements ExperimentResultService {
    
    private final ExperimentResultRepository resultRepository;
    private final ExperimentRepository experimentRepository;
    
    @Override
    @Transactional
    public ExperimentResultDTO createExperimentResult(Long experimentId) {
        log.debug("Creating experiment result for experiment id: {}", experimentId);
        
        // Check if experiment exists
        if (!experimentRepository.existsById(experimentId)) {
            throw new ExperimentNotFoundException("Experiment not found with id: " + experimentId);
        }
        
        String executionId = generateExecutionId();
        LocalDateTime now = LocalDateTime.now();
        
        ExperimentResult result = ExperimentResult.builder()
                .experimentId(experimentId)
                .executionId(executionId)
                .status(ResultStatus.PENDING)
                .startTime(now)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        result = resultRepository.save(result);
        
        return convertToDTO(result);
    }
    
    @Override
    public Optional<ExperimentResultDTO> getResultById(Long id) {
        log.debug("Getting result with id: {}", id);
        
        return resultRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    @Override
    public Optional<ExperimentResultDTO> getResultByExecutionId(String executionId) {
        log.debug("Getting result with execution id: {}", executionId);
        
        return resultRepository.findByExecutionId(executionId)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<ExperimentResultDTO> getResultsByExperimentId(Long experimentId, Pageable pageable) {
        log.debug("Getting results for experiment id: {}", experimentId);
        
        return resultRepository.findByExperimentId(experimentId, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    @Transactional
    public ExperimentResultDTO updateResultStatus(String executionId, ResultStatus status) {
        log.debug("Updating status of result with execution id: {} to {}", executionId, status);
        
        ExperimentResult result = resultRepository.findByExecutionId(executionId)
                .orElseThrow(() -> new ResultNotFoundException("Result not found with execution id: " + executionId));
        
        result.setStatus(status);
        result.setUpdatedAt(LocalDateTime.now());
        
        if (status == ResultStatus.COMPLETED || status == ResultStatus.FAILED || 
            status == ResultStatus.TIMEOUT || status == ResultStatus.CANCELLED) {
            result.setEndTime(LocalDateTime.now());
        }
        
        resultRepository.save(result);
        
        return convertToDTO(result);
    }
    
    @Override
    @Transactional
    public ExperimentResultDTO saveResultData(String executionId, String resultData) {
        log.debug("Saving result data for execution id: {}", executionId);
        
        ExperimentResult result = resultRepository.findByExecutionId(executionId)
                .orElseThrow(() -> new ResultNotFoundException("Result not found with execution id: " + executionId));
        
        result.setResultData(resultData);
        result.setStatus(ResultStatus.COMPLETED);
        result.setEndTime(LocalDateTime.now());
        result.setUpdatedAt(LocalDateTime.now());
        
        resultRepository.save(result);
        
        return convertToDTO(result);
    }
    
    @Override
    public List<ExperimentResultDTO> getResultsByStatus(ResultStatus status) {
        log.debug("Getting results with status: {}", status);
        
        return resultRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public ExperimentResultDTO recordFailure(String executionId, String errorMessage) {
        log.debug("Recording failure for execution id: {} with message: {}", executionId, errorMessage);
        
        ExperimentResult result = resultRepository.findByExecutionId(executionId)
                .orElseThrow(() -> new ResultNotFoundException("Result not found with execution id: " + executionId));
        
        result.setErrorMessage(errorMessage);
        result.setStatus(ResultStatus.FAILED);
        result.setEndTime(LocalDateTime.now());
        result.setUpdatedAt(LocalDateTime.now());
        
        resultRepository.save(result);
        
        return convertToDTO(result);
    }
    
    /**
     * Generate a unique execution ID
     *
     * @return Execution ID
     */
    private String generateExecutionId() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Convert ExperimentResult entity to DTO
     *
     * @param result ExperimentResult entity
     * @return ExperimentResultDTO
     */
    private ExperimentResultDTO convertToDTO(ExperimentResult result) {
        return ExperimentResultDTO.builder()
                .id(result.getId())
                .experimentId(result.getExperimentId())
                .executionId(result.getExecutionId())
                .startTime(result.getStartTime())
                .endTime(result.getEndTime())
                .status(result.getStatus())
                .resultData(result.getResultData())
                .errorMessage(result.getErrorMessage())
                .createdAt(result.getCreatedAt())
                .updatedAt(result.getUpdatedAt())
                .build();
    }
}
