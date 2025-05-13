package org.example.backend.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.backend.common.exception.BusinessException;
import org.example.backend.core.domain.Evaluation;
import org.example.backend.core.repository.EvaluationRepository;
import org.example.backend.core.service.EvaluationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Evaluation service implementation
 */
@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {
    
    private final EvaluationRepository evaluationRepository;
    
    @Override
    @Transactional
    public Evaluation createEvaluation(Evaluation evaluation) {
        // Validate rating (1-5)
        if (evaluation.getRating() < 1 || evaluation.getRating() > 5) {
            throw new BusinessException("Rating must be between 1 and 5");
        }
        
        // Check if user already evaluated this experiment
        Optional<Evaluation> existingEvaluation = evaluationRepository
                .findByExperimentIdAndUserId(evaluation.getExperimentId(), evaluation.getUserId());
        if (existingEvaluation.isPresent()) {
            throw new BusinessException("User already evaluated this experiment");
        }
        
        // Set creation and update time
        LocalDateTime now = LocalDateTime.now();
        evaluation.setCreateTime(now);
        evaluation.setUpdateTime(now);
        
        return evaluationRepository.save(evaluation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Evaluation> findById(String id) {
        return evaluationRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Evaluation> findByExperimentId(String experimentId) {
        return evaluationRepository.findByExperimentId(experimentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Evaluation> findByExperimentId(String experimentId, Pageable pageable) {
        return evaluationRepository.findByExperimentId(experimentId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Evaluation> findByUserId(String userId) {
        return evaluationRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Evaluation> findByUserId(String userId, Pageable pageable) {
        return evaluationRepository.findByUserId(userId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Evaluation> findByExperimentIdAndUserId(String experimentId, String userId) {
        return evaluationRepository.findByExperimentIdAndUserId(experimentId, userId);
    }
    
    @Override
    @Transactional
    public Evaluation updateEvaluation(Evaluation evaluation) {
        // Validate rating (1-5)
        if (evaluation.getRating() < 1 || evaluation.getRating() > 5) {
            throw new BusinessException("Rating must be between 1 and 5");
        }
        
        // Check if evaluation exists
        Evaluation existingEvaluation = evaluationRepository.findById(evaluation.getId())
                .orElseThrow(() -> new BusinessException("Evaluation not found"));
        
        // Check if user is updating their own evaluation
        if (!existingEvaluation.getUserId().equals(evaluation.getUserId())) {
            throw new BusinessException("User can only update their own evaluations");
        }
        
        // Set update time
        evaluation.setUpdateTime(LocalDateTime.now());
        // Preserve creation time
        evaluation.setCreateTime(existingEvaluation.getCreateTime());
        
        return evaluationRepository.save(evaluation);
    }
    
    @Override
    @Transactional
    public void deleteEvaluation(String id) {
        evaluationRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double calculateAverageRating(String experimentId) {
        return evaluationRepository.calculateAverageRatingByExperimentId(experimentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countByExperimentId(String experimentId) {
        return evaluationRepository.countByExperimentId(experimentId);
    }
}
