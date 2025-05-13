package org.example.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.evaluation.domain.SystemFeedback;
import org.example.evaluation.domain.SystemFeedback.FeedbackStatus;
import org.example.evaluation.dto.SystemFeedbackDTO;
import org.example.evaluation.dto.SystemFeedbackResponseDTO;
import org.example.evaluation.repository.SystemFeedbackRepository;
import org.example.evaluation.service.SystemFeedbackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统反馈服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SystemFeedbackServiceImpl implements SystemFeedbackService {

    private final SystemFeedbackRepository feedbackRepository;

    @Override
    @Transactional
    public SystemFeedbackResponseDTO submitFeedback(SystemFeedbackDTO feedbackDTO) {
        log.info("Submitting feedback for user: {}", feedbackDTO.getUserId());
        
        // 创建反馈实体并保存
        SystemFeedback feedback = SystemFeedback.builder()
                .userId(feedbackDTO.getUserId())
                .category(feedbackDTO.getCategory())
                .content(feedbackDTO.getContent())
                .suggestion(feedbackDTO.getSuggestion())
                .status(FeedbackStatus.SUBMITTED) // 初始状态为已提交
                .build();
        
        SystemFeedback savedFeedback = feedbackRepository.save(feedback);
        
        return mapToResponseDTO(savedFeedback);
    }

    @Override
    @Transactional(readOnly = true)
    public SystemFeedbackResponseDTO getFeedback(Long id) {
        log.info("Getting feedback with ID: {}", id);
        
        SystemFeedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found with ID: " + id));
        
        return mapToResponseDTO(feedback);
    }

    @Override
    @Transactional
    public SystemFeedbackResponseDTO updateFeedbackStatus(Long id, FeedbackStatus status, String adminResponse) {
        log.info("Updating feedback status to {} for ID: {}", status, id);
        
        // 获取现有反馈
        SystemFeedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Feedback not found with ID: " + id));
        
        // 更新状态和回复
        feedback.setStatus(status);
        feedback.setAdminResponse(adminResponse);
        
        // 保存更新后的反馈
        SystemFeedback updatedFeedback = feedbackRepository.save(feedback);
        
        return mapToResponseDTO(updatedFeedback);
    }

    @Override
    @Transactional
    public void deleteFeedback(Long id) {
        log.info("Deleting feedback with ID: {}", id);
        
        // 检查反馈是否存在
        if (!feedbackRepository.existsById(id)) {
            throw new EntityNotFoundException("Feedback not found with ID: " + id);
        }
        
        feedbackRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemFeedbackResponseDTO> getFeedbacksByUserId(String userId) {
        log.info("Getting feedbacks for user: {}", userId);
        
        List<SystemFeedback> feedbacks = feedbackRepository.findByUserId(userId);
        
        return feedbacks.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemFeedbackResponseDTO> getFeedbacksByUserId(String userId, Pageable pageable) {
        log.info("Getting paged feedbacks for user: {}", userId);
        
        Page<SystemFeedback> feedbacks = feedbackRepository.findByUserId(userId, pageable);
        
        return feedbacks.map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemFeedbackResponseDTO> getFeedbacksByCategory(String category) {
        log.info("Getting feedbacks for category: {}", category);
        
        List<SystemFeedback> feedbacks = feedbackRepository.findByCategory(category);
        
        return feedbacks.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemFeedbackResponseDTO> getFeedbacksByCategory(String category, Pageable pageable) {
        log.info("Getting paged feedbacks for category: {}", category);
        
        Page<SystemFeedback> feedbacks = feedbackRepository.findByCategory(category, pageable);
        
        return feedbacks.map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemFeedbackResponseDTO> getFeedbacksByStatus(FeedbackStatus status) {
        log.info("Getting feedbacks with status: {}", status);
        
        List<SystemFeedback> feedbacks = feedbackRepository.findByStatus(status);
        
        return feedbacks.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemFeedbackResponseDTO> getFeedbacksByStatus(FeedbackStatus status, Pageable pageable) {
        log.info("Getting paged feedbacks with status: {}", status);
        
        Page<SystemFeedback> feedbacks = feedbackRepository.findByStatus(status, pageable);
        
        return feedbacks.map(this::mapToResponseDTO);
    }
    
    /**
     * 将实体转换为响应DTO
     */
    private SystemFeedbackResponseDTO mapToResponseDTO(SystemFeedback entity) {
        return SystemFeedbackResponseDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .category(entity.getCategory())
                .content(entity.getContent())
                .suggestion(entity.getSuggestion())
                .status(entity.getStatus())
                .adminResponse(entity.getAdminResponse())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
