package org.example.backend.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.backend.common.exception.BusinessException;
import org.example.backend.core.domain.Experiment;
import org.example.backend.core.domain.ExperimentStatus;
import org.example.backend.core.repository.ExperimentRepository;
import org.example.backend.core.service.ExperimentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Experiment service implementation
 */
@Service
@RequiredArgsConstructor
public class ExperimentServiceImpl implements ExperimentService {
    
    private final ExperimentRepository experimentRepository;
    
    @Override
    @Transactional
    public Experiment createExperiment(Experiment experiment) {
        // Set creation and update time
        LocalDateTime now = LocalDateTime.now();
        experiment.setCreateTime(now);
        experiment.setUpdateTime(now);
        
        // Set initial status if not specified
        if (experiment.getStatus() == null) {
            experiment.setStatus(ExperimentStatus.DRAFT);
        }
        
        return experimentRepository.save(experiment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Experiment> findById(String id) {
        return experimentRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Experiment> findByUserId(String userId) {
        return experimentRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Experiment> findByUserId(String userId, Pageable pageable) {
        return experimentRepository.findByUserId(userId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Experiment> findByStatus(ExperimentStatus status) {
        return experimentRepository.findByStatus(status);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Experiment> findByStatus(ExperimentStatus status, Pageable pageable) {
        return experimentRepository.findByStatus(status, pageable);
    }
    
    @Override
    @Transactional
    public Experiment updateExperiment(Experiment experiment) {
        // Check if experiment exists
        Experiment existingExperiment = experimentRepository.findById(experiment.getId())
                .orElseThrow(() -> new BusinessException("Experiment not found"));
        
        // Set update time
        experiment.setUpdateTime(LocalDateTime.now());
        // Preserve creation time
        experiment.setCreateTime(existingExperiment.getCreateTime());
        
        return experimentRepository.save(experiment);
    }
    
    @Override
    @Transactional
    public void deleteExperiment(String id) {
        experimentRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public Experiment changeStatus(String id, ExperimentStatus status) {
        // Find experiment
        Experiment experiment = experimentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Experiment not found"));
        
        // Set new status
        experiment.setStatus(status);
        experiment.setUpdateTime(LocalDateTime.now());
        
        return experimentRepository.save(experiment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Experiment> searchByName(String name, Pageable pageable) {
        return experimentRepository.findByNameContaining(name, pageable);
    }
}
