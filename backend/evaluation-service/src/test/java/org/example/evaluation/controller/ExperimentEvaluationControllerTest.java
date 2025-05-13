package org.example.evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.evaluation.dto.*;
import org.example.evaluation.service.ExperimentEvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExperimentEvaluationController.class)
@AutoConfigureMockMvc(addFilters = false) // 禁用Spring Security过滤器用于测试
@ActiveProfiles("test")
public class ExperimentEvaluationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExperimentEvaluationService evaluationService;

    private ExperimentEvaluationDTO evaluationDTO;
    private ExperimentEvaluationResponseDTO responseDTO;
    private String experimentId = "exp-123";
    private String userId = "user-456";

    @BeforeEach
    void setUp() {
        // 准备测试数据
        evaluationDTO = new ExperimentEvaluationDTO();
        evaluationDTO.setExperimentId(experimentId);
        evaluationDTO.setUserId(userId);
        evaluationDTO.setRating(5);
        evaluationDTO.setComment("Great experiment!");
        evaluationDTO.setCriteriaRatings(new HashSet<>());

        responseDTO = new ExperimentEvaluationResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setExperimentId(experimentId);
        responseDTO.setUserId(userId);
        responseDTO.setRating(5);
        responseDTO.setComment("Great experiment!");
        responseDTO.setCriteriaRatings(new HashSet<>());
        responseDTO.setCreatedAt(LocalDateTime.now());
        responseDTO.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateEvaluation() throws Exception {
        // 模拟服务行为
        when(evaluationService.createEvaluation(any(ExperimentEvaluationDTO.class)))
                .thenReturn(responseDTO);

        // 执行请求并验证结果
        mockMvc.perform(post("/api/evaluations/experiments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evaluationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.experimentId").value(experimentId))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void testGetEvaluation() throws Exception {
        // 模拟服务行为
        when(evaluationService.getEvaluation(1L)).thenReturn(responseDTO);

        // 执行请求并验证结果
        mockMvc.perform(get("/api/evaluations/experiments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.experimentId").value(experimentId));
    }

    @Test
    void testGetEvaluation_NotFound() throws Exception {
        // 模拟服务行为
        when(evaluationService.getEvaluation(999L))
                .thenThrow(new EntityNotFoundException("Evaluation not found with ID: 999"));

        // 执行请求并验证结果
        mockMvc.perform(get("/api/evaluations/experiments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateEvaluation() throws Exception {
        // 模拟服务行为
        when(evaluationService.updateEvaluation(eq(1L), any(ExperimentEvaluationDTO.class)))
                .thenReturn(responseDTO);

        // 执行请求并验证结果
        mockMvc.perform(put("/api/evaluations/experiments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evaluationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.experimentId").value(experimentId));
    }

    @Test
    void testDeleteEvaluation() throws Exception {
        // 执行请求并验证结果
        mockMvc.perform(delete("/api/evaluations/experiments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetEvaluationsByExperimentId() throws Exception {
        // 模拟服务行为
        when(evaluationService.getEvaluationsByExperimentId(experimentId))
                .thenReturn(Collections.singletonList(responseDTO));

        // 执行请求并验证结果
        mockMvc.perform(get("/api/evaluations/experiments?experimentId=" + experimentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responseDTO.getId()))
                .andExpect(jsonPath("$[0].experimentId").value(experimentId));
    }

    @Test
    void testGetEvaluationsByExperimentIdPaged() throws Exception {
        // 模拟服务行为
        PageRequest pageable = PageRequest.of(0, 10);
        when(evaluationService.getEvaluationsByExperimentId(eq(experimentId), any()))
                .thenReturn(new PageImpl<>(Collections.singletonList(responseDTO), pageable, 1));

        // 执行请求并验证结果
        mockMvc.perform(get("/api/evaluations/experiments/paged?experimentId=" + experimentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.content[0].experimentId").value(experimentId))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testGetEvaluationStatistics() throws Exception {
        // 准备统计数据
        EvaluationStatisticsDTO statistics = new EvaluationStatisticsDTO();
        statistics.setExperimentId(experimentId);
        statistics.setAverageRating(4.5);
        statistics.setTotalEvaluations(10);
        statistics.setFiveStarCount(5);
        statistics.setFourStarCount(5);

        // 模拟服务行为
        when(evaluationService.getEvaluationStatistics(experimentId)).thenReturn(statistics);

        // 执行请求并验证结果
        mockMvc.perform(get("/api/evaluations/experiments/" + experimentId + "/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.experimentId").value(experimentId))
                .andExpect(jsonPath("$.averageRating").value(4.5))
                .andExpect(jsonPath("$.totalEvaluations").value(10));
    }
}
