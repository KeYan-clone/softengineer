package org.example.evaluation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.evaluation.domain.SystemFeedback;
import org.example.evaluation.dto.SystemFeedbackDTO;
import org.example.evaluation.dto.SystemFeedbackResponseDTO;
import org.example.evaluation.service.SystemFeedbackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统反馈控制器
 */
@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "系统反馈", description = "系统反馈相关API")
public class SystemFeedbackController {

    private final SystemFeedbackService feedbackService;
    
    @PostMapping
    @Operation(summary = "提交系统反馈", description = "提交用户对系统的反馈")
    public ResponseEntity<SystemFeedbackResponseDTO> submitFeedback(
            @Valid @RequestBody SystemFeedbackDTO feedbackDTO) {
        log.info("Received request to submit feedback from user: {}", feedbackDTO.getUserId());
        SystemFeedbackResponseDTO response = feedbackService.submitFeedback(feedbackDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "获取反馈", description = "根据ID获取反馈详情")
    public ResponseEntity<SystemFeedbackResponseDTO> getFeedback(
            @PathVariable @Parameter(description = "反馈ID") Long id) {
        log.info("Received request to get feedback with ID: {}", id);
        SystemFeedbackResponseDTO response = feedbackService.getFeedback(id);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "更新反馈状态", description = "更新反馈的状态和管理员回复")
    public ResponseEntity<SystemFeedbackResponseDTO> updateFeedbackStatus(
            @PathVariable @Parameter(description = "反馈ID") Long id,
            @RequestParam @Parameter(description = "新状态") SystemFeedback.FeedbackStatus status,
            @RequestParam(required = false) @Parameter(description = "管理员回复") String adminResponse) {
        log.info("Received request to update feedback status to {} for ID: {}", status, id);
        SystemFeedbackResponseDTO response = feedbackService.updateFeedbackStatus(id, status, adminResponse);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除反馈", description = "删除指定的反馈")
    public ResponseEntity<Void> deleteFeedback(
            @PathVariable @Parameter(description = "反馈ID") Long id) {
        log.info("Received request to delete feedback with ID: {}", id);
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户的所有反馈", description = "获取指定用户的所有反馈")
    public ResponseEntity<List<SystemFeedbackResponseDTO>> getFeedbacksByUserId(
            @PathVariable @Parameter(description = "用户ID") String userId) {
        log.info("Received request to get feedbacks for user: {}", userId);
        List<SystemFeedbackResponseDTO> feedbacks = feedbackService.getFeedbacksByUserId(userId);
        return ResponseEntity.ok(feedbacks);
    }
    
    @GetMapping("/user/{userId}/paged")
    @Operation(summary = "分页获取用户的反馈", description = "分页获取指定用户的所有反馈")
    public ResponseEntity<Page<SystemFeedbackResponseDTO>> getFeedbacksByUserIdPaged(
            @PathVariable @Parameter(description = "用户ID") String userId,
            @RequestParam(defaultValue = "0") @Parameter(description = "页码") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "每页大小") int size,
            @RequestParam(defaultValue = "createdAt") @Parameter(description = "排序字段") String sort,
            @RequestParam(defaultValue = "DESC") @Parameter(description = "排序方向") String direction) {
        log.info("Received request to get paged feedbacks for user: {}", userId);
        
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<SystemFeedbackResponseDTO> feedbacks = 
                feedbackService.getFeedbacksByUserId(userId, pageable);
        
        return ResponseEntity.ok(feedbacks);
    }
    
    @GetMapping("/category/{category}")
    @Operation(summary = "根据类别获取反馈", description = "获取指定类别的所有反馈")
    public ResponseEntity<List<SystemFeedbackResponseDTO>> getFeedbacksByCategory(
            @PathVariable @Parameter(description = "反馈类别") String category) {
        log.info("Received request to get feedbacks for category: {}", category);
        List<SystemFeedbackResponseDTO> feedbacks = feedbackService.getFeedbacksByCategory(category);
        return ResponseEntity.ok(feedbacks);
    }
    
    @GetMapping("/category/{category}/paged")
    @Operation(summary = "分页获取特定类别的反馈", description = "分页获取指定类别的所有反馈")
    public ResponseEntity<Page<SystemFeedbackResponseDTO>> getFeedbacksByCategoryPaged(
            @PathVariable @Parameter(description = "反馈类别") String category,
            @RequestParam(defaultValue = "0") @Parameter(description = "页码") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "每页大小") int size,
            @RequestParam(defaultValue = "createdAt") @Parameter(description = "排序字段") String sort,
            @RequestParam(defaultValue = "DESC") @Parameter(description = "排序方向") String direction) {
        log.info("Received request to get paged feedbacks for category: {}", category);
        
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<SystemFeedbackResponseDTO> feedbacks = 
                feedbackService.getFeedbacksByCategory(category, pageable);
        
        return ResponseEntity.ok(feedbacks);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取反馈", description = "获取指定状态的所有反馈")
    public ResponseEntity<List<SystemFeedbackResponseDTO>> getFeedbacksByStatus(
            @PathVariable @Parameter(description = "反馈状态") SystemFeedback.FeedbackStatus status) {
        log.info("Received request to get feedbacks with status: {}", status);
        List<SystemFeedbackResponseDTO> feedbacks = feedbackService.getFeedbacksByStatus(status);
        return ResponseEntity.ok(feedbacks);
    }
    
    @GetMapping("/status/{status}/paged")
    @Operation(summary = "分页获取特定状态的反馈", description = "分页获取指定状态的所有反馈")
    public ResponseEntity<Page<SystemFeedbackResponseDTO>> getFeedbacksByStatusPaged(
            @PathVariable @Parameter(description = "反馈状态") SystemFeedback.FeedbackStatus status,
            @RequestParam(defaultValue = "0") @Parameter(description = "页码") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "每页大小") int size,
            @RequestParam(defaultValue = "createdAt") @Parameter(description = "排序字段") String sort,
            @RequestParam(defaultValue = "DESC") @Parameter(description = "排序方向") String direction) {
        log.info("Received request to get paged feedbacks with status: {}", status);
        
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<SystemFeedbackResponseDTO> feedbacks = 
                feedbackService.getFeedbacksByStatus(status, pageable);
        
        return ResponseEntity.ok(feedbacks);
    }
}
