package org.example.experiment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.experiment.dto.ExperimentCreateDTO;
import org.example.experiment.dto.ExperimentDTO;
import org.example.experiment.dto.ExperimentStepDTO;
import org.example.backend.common.exception.ExperimentException;
import org.example.experiment.model.Experiment;
import org.example.experiment.model.ExperimentStatus;
import org.example.experiment.model.ExperimentStep;
import org.example.experiment.repository.ExperimentRepository;
import org.example.experiment.repository.ExperimentStepRepository;
import org.example.experiment.service.ExperimentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of ExperimentService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExperimentServiceImpl implements ExperimentService {
    
    private final ExperimentRepository experimentRepository;
    private final ExperimentStepRepository experimentStepRepository;
    
    @Override
    @Transactional
    public ExperimentDTO createExperiment(ExperimentCreateDTO createDTO, String userId) {
        log.debug("Creating experiment: {}", createDTO.getName());
        
        Experiment experiment = Experiment.builder()
                .name(createDTO.getName())
                .description(createDTO.getDescription())
                .userId(userId)
                .status(ExperimentStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        // Save experiment to generate ID
        experiment = experimentRepository.save(experiment);
        
        // Create and add steps
        createDTO.getSteps().forEach(stepDTO -> {
            ExperimentStep step = ExperimentStep.builder()
                    .experiment(experiment)
                    .name(stepDTO.getName())
                    .description(stepDTO.getDescription())
                    .sequence(stepDTO.getSequence())
                    .stepType(stepDTO.getStepType())
                    .configuration(stepDTO.getConfiguration())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            experiment.addStep(step);
        });
        
        // Save again with the steps
        experimentRepository.save(experiment);
        
        return convertToDTO(experiment);
    }
      @Override
    @Transactional
    public ExperimentDTO updateExperiment(Long id, ExperimentCreateDTO updateDTO) {
        log.debug("Updating experiment with id: {}", id);
        
        Experiment experiment = experimentRepository.findById(id)
                .orElseThrow(() -> ExperimentException.experimentNotFound(id.toString()));
        
        experiment.setName(updateDTO.getName());
        experiment.setDescription(updateDTO.getDescription());
        experiment.setUpdatedAt(LocalDateTime.now());
        
        // Remove existing steps
        experimentStepRepository.deleteByExperimentId(id);
        experiment.getSteps().clear();
        
        // Add new steps
        updateDTO.getSteps().forEach(stepDTO -> {
            ExperimentStep step = ExperimentStep.builder()
                    .experiment(experiment)
                    .name(stepDTO.getName())
                    .description(stepDTO.getDescription())
                    .sequence(stepDTO.getSequence())
                    .stepType(stepDTO.getStepType())
                    .configuration(stepDTO.getConfiguration())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            experiment.addStep(step);
        });
        
        experimentRepository.save(experiment);
        
        return convertToDTO(experiment);
    }
    
    @Override
    public Optional<ExperimentDTO> getExperimentById(Long id) {
        log.debug("Getting experiment with id: {}", id);
        
        return experimentRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    @Override
    public Optional<ExperimentDTO> getExperimentByIdAndUserId(Long id, String userId) {
        log.debug("Getting experiment with id: {} for user: {}", id, userId);
        
        return experimentRepository.findByIdAndUserId(id, userId)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<ExperimentDTO> getAllExperiments(Pageable pageable) {
        log.debug("Getting all experiments with pagination");
        
        return experimentRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<ExperimentDTO> getExperimentsByUserId(String userId, Pageable pageable) {
        log.debug("Getting experiments for user: {}", userId);
        
        return experimentRepository.findByUserId(userId, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<ExperimentDTO> getExperimentsByStatus(ExperimentStatus status, Pageable pageable) {
        log.debug("Getting experiments with status: {}", status);
        
        return experimentRepository.findByStatus(status, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<ExperimentDTO> getExperimentsByUserIdAndStatus(String userId, ExperimentStatus status, Pageable pageable) {
        log.debug("Getting experiments for user: {} with status: {}", userId, status);
        
        return experimentRepository.findByUserIdAndStatus(userId, status, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<ExperimentDTO> searchExperimentsByName(String name, Pageable pageable) {
        log.debug("Searching experiments with name containing: {}", name);
        
        return experimentRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    @Transactional
    public ExperimentDTO updateExperimentStatus(Long id, ExperimentStatus status) {
        log.debug("Updating status of experiment with id: {} to {}", id, status);
          Experiment experiment = experimentRepository.findById(id)
                .orElseThrow(() -> ExperimentException.experimentNotFound(id.toString()));
        
        experiment.setStatus(status);
        experiment.setUpdatedAt(LocalDateTime.now());
        
        experimentRepository.save(experiment);
        
        return convertToDTO(experiment);
    }
    
    @Override
    @Transactional
    public boolean deleteExperiment(Long id) {
        log.debug("Deleting experiment with id: {}", id);
        
        if (!experimentRepository.existsById(id)) {
            return false;
        }
        
        experimentRepository.deleteById(id);
        return true;
    }
    
    @Override
    @Transactional
    public ExperimentDTO shareExperiment(Long id) {
        log.debug("Sharing experiment with id: {}", id);
        
        return updateExperimentStatus(id, ExperimentStatus.PUBLISHED);
    }
    
    @Override
    public Page<ExperimentDTO> getPublishedExperiments(Pageable pageable) {
        log.debug("Getting published experiments");
        
        return experimentRepository.findPublishedExperiments(pageable)
                .map(this::convertToDTO);
    }
    
    /**
     * Convert Experiment entity to DTO
     *
     * @param experiment Experiment entity
     * @return ExperimentDTO
     */
    private ExperimentDTO convertToDTO(Experiment experiment) {
        List<ExperimentStepDTO> stepDTOs = experiment.getSteps().stream()
                .map(step -> ExperimentStepDTO.builder()
                        .id(step.getId())
                        .experimentId(experiment.getId())
                        .name(step.getName())
                        .description(step.getDescription())
                        .sequence(step.getSequence())
                        .stepType(step.getStepType())
                        .configuration(step.getConfiguration())
                        .createdAt(step.getCreatedAt())
                        .updatedAt(step.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
        
        return ExperimentDTO.builder()
                .id(experiment.getId())
                .name(experiment.getName())
                .description(experiment.getDescription())
                .userId(experiment.getUserId())
                .status(experiment.getStatus())
                .createdAt(experiment.getCreatedAt())
                .updatedAt(experiment.getUpdatedAt())
                .steps(stepDTOs)
                .build();
    }
}
