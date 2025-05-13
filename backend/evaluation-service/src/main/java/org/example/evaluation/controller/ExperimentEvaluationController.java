package org.example.evaluation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.evaluation.dto.ExperimentEvaluationDTO;
import org.example.evaluation.dto.ExperimentEvaluationResponseDTO;
import org.example.evaluation.dto.EvaluationStatisticsDTO;
import org.example.evaluation.service.ExperimentEvaluationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 实验评价控制器
 */
@RestController
@RequestMapping("/api/evaluations/experiments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "实验评价", description = "实验评价相关API")
public class ExperimentEvaluationController {

    private final ExperimentEvaluationService evaluationService;
    
    @PostMapping
    @Operation(summary = "创建实验评价", description = "提交用户对实验的评价")
    public ResponseEntity<ExperimentEvaluationResponseDTO> createEvaluation(
            @Valid @RequestBody ExperimentEvaluationDTO evaluationDTO) {
        log.info("Received request to create evaluation for experiment: {}", evaluationDTO.getExperimentId());
        ExperimentEvaluationResponseDTO response = evaluationService.createEvaluation(evaluationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "获取评价", description = "根据ID获取评价详情")
    public ResponseEntity<ExperimentEvaluationResponseDTO> getEvaluation(
            @PathVariable @Parameter(description = "评价ID") Long id) {
        log.info("Received request to get evaluation with ID: {}", id);
        ExperimentEvaluationResponseDTO response = evaluationService.getEvaluation(id);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新评价", description = "更新已有的评价")
    public ResponseEntity<ExperimentEvaluationResponseDTO> updateEvaluation(
            @PathVariable @Parameter(description = "评价ID") Long id,
            @Valid @RequestBody ExperimentEvaluationDTO evaluationDTO) {
        log.info("Received request to update evaluation with ID: {}", id);
        ExperimentEvaluationResponseDTO response = evaluationService.updateEvaluation(id, evaluationDTO);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "删除评价", description = "删除指定的评价")
    public ResponseEntity<Void> deleteEvaluation(
            @PathVariable @Parameter(description = "评价ID") Long id) {
        log.info("Received request to delete evaluation with ID: {}", id);
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping
    @Operation(summary = "获取实验的所有评价", description = "根据实验ID获取所有评价")
    public ResponseEntity<List<ExperimentEvaluationResponseDTO>> getEvaluationsByExperimentId(
            @RequestParam @Parameter(description = "实验ID") String experimentId) {
        log.info("Received request to get evaluations for experiment: {}", experimentId);
        List<ExperimentEvaluationResponseDTO> evaluations = evaluationService.getEvaluationsByExperimentId(experimentId);
        return ResponseEntity.ok(evaluations);
    }
    
    @GetMapping("/paged")
    @Operation(summary = "分页获取实验的评价", description = "根据实验ID分页获取评价")
    public ResponseEntity<Page<ExperimentEvaluationResponseDTO>> getEvaluationsByExperimentIdPaged(
            @RequestParam @Parameter(description = "实验ID") String experimentId,
            @RequestParam(defaultValue = "0") @Parameter(description = "页码") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "每页大小") int size,
            @RequestParam(defaultValue = "createdAt") @Parameter(description = "排序字段") String sort,
            @RequestParam(defaultValue = "DESC") @Parameter(description = "排序方向") String direction) {
        log.info("Received request to get paged evaluations for experiment: {}", experimentId);
        
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<ExperimentEvaluationResponseDTO> evaluations = 
                evaluationService.getEvaluationsByExperimentId(experimentId, pageable);
        
        return ResponseEntity.ok(evaluations);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户的所有评价", description = "获取指定用户的所有评价")
    public ResponseEntity<List<ExperimentEvaluationResponseDTO>> getEvaluationsByUserId(
            @PathVariable @Parameter(description = "用户ID") String userId) {
        log.info("Received request to get evaluations for user: {}", userId);
        List<ExperimentEvaluationResponseDTO> evaluations = evaluationService.getEvaluationsByUserId(userId);
        return ResponseEntity.ok(evaluations);
    }
    
    @GetMapping("/user/{userId}/paged")
    @Operation(summary = "分页获取用户的评价", description = "分页获取指定用户的所有评价")
    public ResponseEntity<Page<ExperimentEvaluationResponseDTO>> getEvaluationsByUserIdPaged(
            @PathVariable @Parameter(description = "用户ID") String userId,
            @RequestParam(defaultValue = "0") @Parameter(description = "页码") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "每页大小") int size,
            @RequestParam(defaultValue = "createdAt") @Parameter(description = "排序字段") String sort,
            @RequestParam(defaultValue = "DESC") @Parameter(description = "排序方向") String direction) {
        log.info("Received request to get paged evaluations for user: {}", userId);
        
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<ExperimentEvaluationResponseDTO> evaluations = 
                evaluationService.getEvaluationsByUserId(userId, pageable);
        
        return ResponseEntity.ok(evaluations);
    }
    
    @GetMapping("/{experimentId}/user/{userId}")
    @Operation(summary = "获取用户对实验的评价", description = "获取指定用户对指定实验的评价")
    public ResponseEntity<ExperimentEvaluationResponseDTO> getUserExperimentEvaluation(
            @PathVariable @Parameter(description = "实验ID") String experimentId,
            @PathVariable @Parameter(description = "用户ID") String userId) {
        log.info("Received request to get evaluation for experiment: {} by user: {}", experimentId, userId);
        
        ExperimentEvaluationResponseDTO evaluation = 
                evaluationService.getUserExperimentEvaluation(experimentId, userId);
        
        if (evaluation == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(evaluation);
    }
    
    @GetMapping("/{experimentId}/statistics")
    @Operation(summary = "获取实验评价统计数据", description = "获取指定实验的评价统计信息")
    public ResponseEntity<EvaluationStatisticsDTO> getEvaluationStatistics(
            @PathVariable @Parameter(description = "实验ID") String experimentId) {
        log.info("Received request to get evaluation statistics for experiment: {}", experimentId);
        EvaluationStatisticsDTO statistics = evaluationService.getEvaluationStatistics(experimentId);
        return ResponseEntity.ok(statistics);
    }
}
