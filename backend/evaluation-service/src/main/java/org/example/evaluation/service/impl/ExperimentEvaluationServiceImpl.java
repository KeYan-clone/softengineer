package org.example.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.evaluation.domain.CriteriaRating;
import org.example.evaluation.domain.ExperimentEvaluation;
import org.example.evaluation.dto.CriteriaRatingDTO;
import org.example.evaluation.dto.ExperimentEvaluationDTO;
import org.example.evaluation.dto.ExperimentEvaluationResponseDTO;
import org.example.evaluation.dto.EvaluationStatisticsDTO;
import org.example.evaluation.repository.ExperimentEvaluationRepository;
import org.example.evaluation.service.ExperimentEvaluationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 实验评价服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExperimentEvaluationServiceImpl implements ExperimentEvaluationService {

    private final ExperimentEvaluationRepository evaluationRepository;

    @Override
    @Transactional
    public ExperimentEvaluationResponseDTO createEvaluation(ExperimentEvaluationDTO evaluationDTO) {
        log.info("Creating evaluation for experiment: {}", evaluationDTO.getExperimentId());
        
        // 检查用户是否已评价该实验
        evaluationRepository.findByExperimentIdAndUserId(evaluationDTO.getExperimentId(), evaluationDTO.getUserId())
                .ifPresent(existing -> {
                    throw new IllegalStateException("User has already evaluated this experiment");
                });
        
        // 创建评价实体并保存
        ExperimentEvaluation evaluation = mapToEntity(evaluationDTO);
        ExperimentEvaluation savedEvaluation = evaluationRepository.save(evaluation);
        
        return mapToResponseDTO(savedEvaluation);
    }

    @Override
    @Transactional
    public ExperimentEvaluationResponseDTO updateEvaluation(Long id, ExperimentEvaluationDTO evaluationDTO) {
        log.info("Updating evaluation with ID: {}", id);
        
        // 获取现有评价
        ExperimentEvaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evaluation not found with ID: " + id));
        
        // 验证是否为同一用户的评价
        if (!evaluation.getUserId().equals(evaluationDTO.getUserId())) {
            throw new IllegalStateException("Cannot update evaluation created by another user");
        }
        
        // 更新评价信息
        evaluation.setRating(evaluationDTO.getRating());
        evaluation.setComment(evaluationDTO.getComment());
        evaluation.getCriteriaRatings().clear();
        
        // 更新评价标准评分
        Set<CriteriaRating> criteriaRatings = evaluationDTO.getCriteriaRatings().stream()
                .map(this::mapToCriteriaRating)
                .collect(Collectors.toSet());
        evaluation.getCriteriaRatings().addAll(criteriaRatings);
        
        // 保存更新后的评价
        ExperimentEvaluation updatedEvaluation = evaluationRepository.save(evaluation);
        
        return mapToResponseDTO(updatedEvaluation);
    }

    @Override
    @Transactional(readOnly = true)
    public ExperimentEvaluationResponseDTO getEvaluation(Long id) {
        log.info("Getting evaluation with ID: {}", id);
        
        ExperimentEvaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evaluation not found with ID: " + id));
        
        return mapToResponseDTO(evaluation);
    }

    @Override
    @Transactional
    public void deleteEvaluation(Long id) {
        log.info("Deleting evaluation with ID: {}", id);
        
        // 检查评价是否存在
        if (!evaluationRepository.existsById(id)) {
            throw new EntityNotFoundException("Evaluation not found with ID: " + id);
        }
        
        evaluationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentEvaluationResponseDTO> getEvaluationsByExperimentId(String experimentId) {
        log.info("Getting evaluations for experiment: {}", experimentId);
        
        List<ExperimentEvaluation> evaluations = evaluationRepository.findByExperimentId(experimentId);
        
        return evaluations.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExperimentEvaluationResponseDTO> getEvaluationsByExperimentId(String experimentId, Pageable pageable) {
        log.info("Getting paged evaluations for experiment: {}", experimentId);
        
        Page<ExperimentEvaluation> evaluations = evaluationRepository.findByExperimentId(experimentId, pageable);
        
        return evaluations.map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperimentEvaluationResponseDTO> getEvaluationsByUserId(String userId) {
        log.info("Getting evaluations for user: {}", userId);
        
        List<ExperimentEvaluation> evaluations = evaluationRepository.findByUserId(userId);
        
        return evaluations.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExperimentEvaluationResponseDTO> getEvaluationsByUserId(String userId, Pageable pageable) {
        log.info("Getting paged evaluations for user: {}", userId);
        
        Page<ExperimentEvaluation> evaluations = evaluationRepository.findByUserId(userId, pageable);
        
        return evaluations.map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ExperimentEvaluationResponseDTO getUserExperimentEvaluation(String experimentId, String userId) {
        log.info("Getting evaluation for experiment: {} by user: {}", experimentId, userId);
        
        return evaluationRepository.findByExperimentIdAndUserId(experimentId, userId)
                .map(this::mapToResponseDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluationStatisticsDTO getEvaluationStatistics(String experimentId) {
        log.info("Getting evaluation statistics for experiment: {}", experimentId);
        
        Double averageRating = evaluationRepository.calculateAverageRatingByExperimentId(experimentId);
        long totalEvaluations = evaluationRepository.countByExperimentId(experimentId);
        
        // 获取各星级评价数量
        List<ExperimentEvaluation> evaluations = evaluationRepository.findByExperimentId(experimentId);
        long oneStarCount = evaluations.stream().filter(e -> e.getRating() == 1).count();
        long twoStarCount = evaluations.stream().filter(e -> e.getRating() == 2).count();
        long threeStarCount = evaluations.stream().filter(e -> e.getRating() == 3).count();
        long fourStarCount = evaluations.stream().filter(e -> e.getRating() == 4).count();
        long fiveStarCount = evaluations.stream().filter(e -> e.getRating() == 5).count();
        
        return EvaluationStatisticsDTO.builder()
                .experimentId(experimentId)
                .averageRating(averageRating != null ? averageRating : 0.0)
                .totalEvaluations(totalEvaluations)
                .oneStarCount(oneStarCount)
                .twoStarCount(twoStarCount)
                .threeStarCount(threeStarCount)
                .fourStarCount(fourStarCount)
                .fiveStarCount(fiveStarCount)
                .build();
    }
    
    /**
     * 将DTO转换为实体
     */
    private ExperimentEvaluation mapToEntity(ExperimentEvaluationDTO dto) {
        ExperimentEvaluation evaluation = ExperimentEvaluation.builder()
                .experimentId(dto.getExperimentId())
                .userId(dto.getUserId())
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();
        
        // 添加评价标准评分
        Set<CriteriaRating> criteriaRatings = dto.getCriteriaRatings().stream()
                .map(this::mapToCriteriaRating)
                .collect(Collectors.toSet());
        evaluation.setCriteriaRatings(criteriaRatings);
        
        return evaluation;
    }
    
    /**
     * 将评价标准DTO转换为实体
     */
    private CriteriaRating mapToCriteriaRating(CriteriaRatingDTO dto) {
        return CriteriaRating.builder()
                .criteriaName(dto.getCriteriaName())
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();
    }
    
    /**
     * 将实体转换为响应DTO
     */
    private ExperimentEvaluationResponseDTO mapToResponseDTO(ExperimentEvaluation entity) {
        Set<CriteriaRatingDTO> criteriaRatingDTOs = entity.getCriteriaRatings().stream()
                .map(cr -> new CriteriaRatingDTO(cr.getCriteriaName(), cr.getRating(), cr.getComment()))
                .collect(Collectors.toSet());
        
        return ExperimentEvaluationResponseDTO.builder()
                .id(entity.getId())
                .experimentId(entity.getExperimentId())
                .userId(entity.getUserId())
                .rating(entity.getRating())
                .comment(entity.getComment())
                .criteriaRatings(criteriaRatingDTOs)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
