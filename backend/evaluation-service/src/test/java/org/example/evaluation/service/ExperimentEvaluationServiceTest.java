package org.example.evaluation.service;

import org.example.evaluation.domain.ExperimentEvaluation;
import org.example.evaluation.dto.CriteriaRatingDTO;
import org.example.evaluation.dto.ExperimentEvaluationDTO;
import org.example.evaluation.dto.ExperimentEvaluationResponseDTO;
import org.example.evaluation.repository.ExperimentEvaluationRepository;
import org.example.evaluation.service.impl.ExperimentEvaluationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExperimentEvaluationServiceTest {

    @Mock
    private ExperimentEvaluationRepository evaluationRepository;

    @InjectMocks
    private ExperimentEvaluationServiceImpl evaluationService;

    private ExperimentEvaluation sampleEvaluation;
    private ExperimentEvaluationDTO sampleDto;
    private String experimentId = "exp-123";
    private String userId = "user-456";

    @BeforeEach
    void setUp() {
        // 准备测试数据
        sampleEvaluation = new ExperimentEvaluation();
        sampleEvaluation.setId(1L);
        sampleEvaluation.setExperimentId(experimentId);
        sampleEvaluation.setUserId(userId);
        sampleEvaluation.setRating(5);
        sampleEvaluation.setComment("Great experiment!");
        sampleEvaluation.setCreatedAt(LocalDateTime.now());
        sampleEvaluation.setUpdatedAt(LocalDateTime.now());
        sampleEvaluation.setCriteriaRatings(new HashSet<>());

        sampleDto = new ExperimentEvaluationDTO();
        sampleDto.setExperimentId(experimentId);
        sampleDto.setUserId(userId);
        sampleDto.setRating(5);
        sampleDto.setComment("Great experiment!");
        sampleDto.setCriteriaRatings(new HashSet<>());
    }

    @Test
    void testCreateEvaluation() {
        // 模拟依赖行为
        when(evaluationRepository.findByExperimentIdAndUserId(experimentId, userId))
                .thenReturn(Optional.empty());
        when(evaluationRepository.save(any(ExperimentEvaluation.class)))
                .thenReturn(sampleEvaluation);

        // 执行测试
        ExperimentEvaluationResponseDTO result = evaluationService.createEvaluation(sampleDto);

        // 验证结果
        assertNotNull(result);
        assertEquals(sampleEvaluation.getId(), result.getId());
        assertEquals(sampleEvaluation.getExperimentId(), result.getExperimentId());
        assertEquals(sampleEvaluation.getUserId(), result.getUserId());
        assertEquals(sampleEvaluation.getRating(), result.getRating());
        assertEquals(sampleEvaluation.getComment(), result.getComment());

        // 验证交互
        verify(evaluationRepository).findByExperimentIdAndUserId(experimentId, userId);
        verify(evaluationRepository).save(any(ExperimentEvaluation.class));
    }

    @Test
    void testCreateEvaluation_UserAlreadyEvaluated() {
        // 模拟用户已评价的情况
        when(evaluationRepository.findByExperimentIdAndUserId(experimentId, userId))
                .thenReturn(Optional.of(sampleEvaluation));

        // 验证异常
        assertThrows(IllegalStateException.class, () -> {
            evaluationService.createEvaluation(sampleDto);
        });

        // 验证交互
        verify(evaluationRepository).findByExperimentIdAndUserId(experimentId, userId);
        verify(evaluationRepository, never()).save(any(ExperimentEvaluation.class));
    }

    @Test
    void testGetEvaluation() {
        // 模拟依赖行为
        when(evaluationRepository.findById(1L)).thenReturn(Optional.of(sampleEvaluation));

        // 执行测试
        ExperimentEvaluationResponseDTO result = evaluationService.getEvaluation(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(sampleEvaluation.getId(), result.getId());
        assertEquals(sampleEvaluation.getExperimentId(), result.getExperimentId());

        // 验证交互
        verify(evaluationRepository).findById(1L);
    }

    @Test
    void testGetEvaluation_NotFound() {
        // 模拟评价不存在的情况
        when(evaluationRepository.findById(999L)).thenReturn(Optional.empty());

        // 验证异常
        assertThrows(EntityNotFoundException.class, () -> {
            evaluationService.getEvaluation(999L);
        });

        // 验证交互
        verify(evaluationRepository).findById(999L);
    }

    @Test
    void testGetEvaluationsByExperimentId() {
        // 模拟依赖行为
        List<ExperimentEvaluation> evaluations = Collections.singletonList(sampleEvaluation);
        when(evaluationRepository.findByExperimentId(experimentId)).thenReturn(evaluations);

        // 执行测试
        List<ExperimentEvaluationResponseDTO> results = evaluationService.getEvaluationsByExperimentId(experimentId);

        // 验证结果
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(sampleEvaluation.getId(), results.get(0).getId());

        // 验证交互
        verify(evaluationRepository).findByExperimentId(experimentId);
    }

    @Test
    void testGetEvaluationsByExperimentIdPaged() {
        // 模拟依赖行为
        List<ExperimentEvaluation> evaluations = Collections.singletonList(sampleEvaluation);
        Page<ExperimentEvaluation> pagedEvaluations = new PageImpl<>(evaluations);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(evaluationRepository.findByExperimentId(experimentId, pageable)).thenReturn(pagedEvaluations);

        // 执行测试
        Page<ExperimentEvaluationResponseDTO> results = 
                evaluationService.getEvaluationsByExperimentId(experimentId, pageable);

        // 验证结果
        assertNotNull(results);
        assertEquals(1, results.getTotalElements());
        assertEquals(sampleEvaluation.getId(), results.getContent().get(0).getId());

        // 验证交互
        verify(evaluationRepository).findByExperimentId(experimentId, pageable);
    }

    @Test
    void testDeleteEvaluation() {
        // 模拟依赖行为
        when(evaluationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(evaluationRepository).deleteById(1L);

        // 执行测试
        evaluationService.deleteEvaluation(1L);

        // 验证交互
        verify(evaluationRepository).existsById(1L);
        verify(evaluationRepository).deleteById(1L);
    }

    @Test
    void testDeleteEvaluation_NotFound() {
        // 模拟评价不存在的情况
        when(evaluationRepository.existsById(999L)).thenReturn(false);

        // 验证异常
        assertThrows(EntityNotFoundException.class, () -> {
            evaluationService.deleteEvaluation(999L);
        });

        // 验证交互
        verify(evaluationRepository).existsById(999L);
        verify(evaluationRepository, never()).deleteById(999L);
    }
}
