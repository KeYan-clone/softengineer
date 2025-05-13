package org.example.evaluation.service;

import org.example.evaluation.domain.SystemFeedback;
import org.example.evaluation.dto.SystemFeedbackDTO;
import org.example.evaluation.dto.SystemFeedbackResponseDTO;
import org.example.evaluation.repository.SystemFeedbackRepository;
import org.example.evaluation.service.impl.SystemFeedbackServiceImpl;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SystemFeedbackServiceTest {

    @Mock
    private SystemFeedbackRepository feedbackRepository;

    @InjectMocks
    private SystemFeedbackServiceImpl feedbackService;

    private SystemFeedback sampleFeedback;
    private SystemFeedbackDTO sampleDto;
    private String userId = "user-456";
    private String category = "interface";

    @BeforeEach
    void setUp() {
        // 准备测试数据
        sampleFeedback = new SystemFeedback();
        sampleFeedback.setId(1L);
        sampleFeedback.setUserId(userId);
        sampleFeedback.setCategory(category);
        sampleFeedback.setContent("The interface is not responsive on mobile");
        sampleFeedback.setSuggestion("Please make it more mobile-friendly");
        sampleFeedback.setStatus(SystemFeedback.FeedbackStatus.SUBMITTED);
        sampleFeedback.setCreatedAt(LocalDateTime.now());
        sampleFeedback.setUpdatedAt(LocalDateTime.now());

        sampleDto = new SystemFeedbackDTO();
        sampleDto.setUserId(userId);
        sampleDto.setCategory(category);
        sampleDto.setContent("The interface is not responsive on mobile");
        sampleDto.setSuggestion("Please make it more mobile-friendly");
    }

    @Test
    void testSubmitFeedback() {
        // 模拟依赖行为
        when(feedbackRepository.save(any(SystemFeedback.class)))
                .thenReturn(sampleFeedback);

        // 执行测试
        SystemFeedbackResponseDTO result = feedbackService.submitFeedback(sampleDto);

        // 验证结果
        assertNotNull(result);
        assertEquals(sampleFeedback.getId(), result.getId());
        assertEquals(sampleFeedback.getUserId(), result.getUserId());
        assertEquals(sampleFeedback.getCategory(), result.getCategory());
        assertEquals(sampleFeedback.getStatus(), result.getStatus());

        // 验证交互
        verify(feedbackRepository).save(any(SystemFeedback.class));
    }

    @Test
    void testGetFeedback() {
        // 模拟依赖行为
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(sampleFeedback));

        // 执行测试
        SystemFeedbackResponseDTO result = feedbackService.getFeedback(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(sampleFeedback.getId(), result.getId());
        assertEquals(sampleFeedback.getUserId(), result.getUserId());
        assertEquals(sampleFeedback.getCategory(), result.getCategory());

        // 验证交互
        verify(feedbackRepository).findById(1L);
    }

    @Test
    void testGetFeedback_NotFound() {
        // 模拟反馈不存在的情况
        when(feedbackRepository.findById(999L)).thenReturn(Optional.empty());

        // 验证异常
        assertThrows(EntityNotFoundException.class, () -> {
            feedbackService.getFeedback(999L);
        });

        // 验证交互
        verify(feedbackRepository).findById(999L);
    }

    @Test
    void testUpdateFeedbackStatus() {
        // 模拟依赖行为
        SystemFeedback updatedFeedback = sampleFeedback;
        updatedFeedback.setStatus(SystemFeedback.FeedbackStatus.PROCESSING);
        updatedFeedback.setAdminResponse("We are looking into this issue");
        
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(sampleFeedback));
        when(feedbackRepository.save(any(SystemFeedback.class))).thenReturn(updatedFeedback);

        // 执行测试
        SystemFeedbackResponseDTO result = feedbackService.updateFeedbackStatus(
                1L, 
                SystemFeedback.FeedbackStatus.PROCESSING, 
                "We are looking into this issue");

        // 验证结果
        assertNotNull(result);
        assertEquals(SystemFeedback.FeedbackStatus.PROCESSING, result.getStatus());
        assertEquals("We are looking into this issue", result.getAdminResponse());

        // 验证交互
        verify(feedbackRepository).findById(1L);
        verify(feedbackRepository).save(any(SystemFeedback.class));
    }

    @Test
    void testUpdateFeedbackStatus_NotFound() {
        // 模拟反馈不存在的情况
        when(feedbackRepository.findById(999L)).thenReturn(Optional.empty());

        // 验证异常
        assertThrows(EntityNotFoundException.class, () -> {
            feedbackService.updateFeedbackStatus(
                    999L, 
                    SystemFeedback.FeedbackStatus.PROCESSING, 
                    "We are looking into this issue");
        });

        // 验证交互
        verify(feedbackRepository).findById(999L);
        verify(feedbackRepository, never()).save(any(SystemFeedback.class));
    }

    @Test
    void testGetFeedbacksByUserId() {
        // 模拟依赖行为
        List<SystemFeedback> feedbacks = Collections.singletonList(sampleFeedback);
        when(feedbackRepository.findByUserId(userId)).thenReturn(feedbacks);

        // 执行测试
        List<SystemFeedbackResponseDTO> results = feedbackService.getFeedbacksByUserId(userId);

        // 验证结果
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(sampleFeedback.getId(), results.get(0).getId());

        // 验证交互
        verify(feedbackRepository).findByUserId(userId);
    }

    @Test
    void testGetFeedbacksByUserIdPaged() {
        // 模拟依赖行为
        List<SystemFeedback> feedbacks = Collections.singletonList(sampleFeedback);
        Page<SystemFeedback> pagedFeedbacks = new PageImpl<>(feedbacks);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(feedbackRepository.findByUserId(userId, pageable)).thenReturn(pagedFeedbacks);

        // 执行测试
        Page<SystemFeedbackResponseDTO> results = 
                feedbackService.getFeedbacksByUserId(userId, pageable);

        // 验证结果
        assertNotNull(results);
        assertEquals(1, results.getTotalElements());
        assertEquals(sampleFeedback.getId(), results.getContent().get(0).getId());

        // 验证交互
        verify(feedbackRepository).findByUserId(userId, pageable);
    }

    @Test
    void testGetFeedbacksByCategory() {
        // 模拟依赖行为
        List<SystemFeedback> feedbacks = Collections.singletonList(sampleFeedback);
        when(feedbackRepository.findByCategory(category)).thenReturn(feedbacks);

        // 执行测试
        List<SystemFeedbackResponseDTO> results = feedbackService.getFeedbacksByCategory(category);

        // 验证结果
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(sampleFeedback.getId(), results.get(0).getId());
        assertEquals(category, results.get(0).getCategory());

        // 验证交互
        verify(feedbackRepository).findByCategory(category);
    }

    @Test
    void testDeleteFeedback() {
        // 模拟依赖行为
        when(feedbackRepository.existsById(1L)).thenReturn(true);
        doNothing().when(feedbackRepository).deleteById(1L);

        // 执行测试
        feedbackService.deleteFeedback(1L);

        // 验证交互
        verify(feedbackRepository).existsById(1L);
        verify(feedbackRepository).deleteById(1L);
    }

    @Test
    void testDeleteFeedback_NotFound() {
        // 模拟反馈不存在的情况
        when(feedbackRepository.existsById(999L)).thenReturn(false);

        // 验证异常
        assertThrows(EntityNotFoundException.class, () -> {
            feedbackService.deleteFeedback(999L);
        });

        // 验证交互
        verify(feedbackRepository).existsById(999L);
        verify(feedbackRepository, never()).deleteById(999L);
    }
}
