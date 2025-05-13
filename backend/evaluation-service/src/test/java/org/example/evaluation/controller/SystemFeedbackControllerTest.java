package org.example.evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.evaluation.domain.SystemFeedback;
import org.example.evaluation.dto.SystemFeedbackDTO;
import org.example.evaluation.dto.SystemFeedbackResponseDTO;
import org.example.evaluation.service.SystemFeedbackService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SystemFeedbackController.class)
@AutoConfigureMockMvc(addFilters = false) // 禁用Spring Security过滤器用于测试
@ActiveProfiles("test")
public class SystemFeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SystemFeedbackService feedbackService;

    private SystemFeedbackDTO feedbackDTO;
    private SystemFeedbackResponseDTO responseDTO;
    private String userId = "user-456";
    private String category = "interface";

    @BeforeEach
    void setUp() {
        // 准备测试数据
        feedbackDTO = new SystemFeedbackDTO();
        feedbackDTO.setUserId(userId);
        feedbackDTO.setCategory(category);
        feedbackDTO.setContent("The interface is not responsive on mobile");
        feedbackDTO.setSuggestion("Please make it more mobile-friendly");

        responseDTO = new SystemFeedbackResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setUserId(userId);
        responseDTO.setCategory(category);
        responseDTO.setContent("The interface is not responsive on mobile");
        responseDTO.setSuggestion("Please make it more mobile-friendly");
        responseDTO.setStatus(SystemFeedback.FeedbackStatus.SUBMITTED);
        responseDTO.setCreatedAt(LocalDateTime.now());
        responseDTO.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testSubmitFeedback() throws Exception {
        // 模拟服务行为
        when(feedbackService.submitFeedback(any(SystemFeedbackDTO.class)))
                .thenReturn(responseDTO);

        // 执行请求并验证结果
        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedbackDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.category").value(category))
                .andExpect(jsonPath("$.status").value("SUBMITTED"));
    }

    @Test
    void testGetFeedback() throws Exception {
        // 模拟服务行为
        when(feedbackService.getFeedback(1L)).thenReturn(responseDTO);

        // 执行请求并验证结果
        mockMvc.perform(get("/api/feedback/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.category").value(category));
    }

    @Test
    void testGetFeedback_NotFound() throws Exception {
        // 模拟服务行为
        when(feedbackService.getFeedback(999L))
                .thenThrow(new EntityNotFoundException("Feedback not found with ID: 999"));

        // 执行请求并验证结果
        mockMvc.perform(get("/api/feedback/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateFeedbackStatus() throws Exception {
        // 修改响应数据状态
        SystemFeedbackResponseDTO updatedResponse = responseDTO;
        updatedResponse.setStatus(SystemFeedback.FeedbackStatus.PROCESSING);
        updatedResponse.setAdminResponse("We are looking into this issue");

        // 模拟服务行为
        when(feedbackService.updateFeedbackStatus(
                eq(1L), 
                eq(SystemFeedback.FeedbackStatus.PROCESSING), 
                eq("We are looking into this issue")))
                .thenReturn(updatedResponse);

        // 执行请求并验证结果
        mockMvc.perform(put("/api/feedback/1/status")
                .param("status", "PROCESSING")
                .param("adminResponse", "We are looking into this issue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedResponse.getId()))
                .andExpect(jsonPath("$.status").value("PROCESSING"))
                .andExpect(jsonPath("$.adminResponse").value("We are looking into this issue"));
    }

    @Test
    void testDeleteFeedback() throws Exception {
        // 执行请求并验证结果
        mockMvc.perform(delete("/api/feedback/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetFeedbacksByUserId() throws Exception {
        // 模拟服务行为
        when(feedbackService.getFeedbacksByUserId(userId))
                .thenReturn(Collections.singletonList(responseDTO));

        // 执行请求并验证结果
        mockMvc.perform(get("/api/feedback/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responseDTO.getId()))
                .andExpect(jsonPath("$[0].userId").value(userId));
    }

    @Test
    void testGetFeedbacksByUserIdPaged() throws Exception {
        // 模拟服务行为
        PageRequest pageable = PageRequest.of(0, 10);
        when(feedbackService.getFeedbacksByUserId(eq(userId), any()))
                .thenReturn(new PageImpl<>(Collections.singletonList(responseDTO), pageable, 1));

        // 执行请求并验证结果
        mockMvc.perform(get("/api/feedback/user/" + userId + "/paged"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.content[0].userId").value(userId))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testGetFeedbacksByCategory() throws Exception {
        // 模拟服务行为
        when(feedbackService.getFeedbacksByCategory(category))
                .thenReturn(Collections.singletonList(responseDTO));

        // 执行请求并验证结果
        mockMvc.perform(get("/api/feedback/category/" + category))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responseDTO.getId()))
                .andExpect(jsonPath("$[0].category").value(category));
    }

    @Test
    void testGetFeedbacksByStatus() throws Exception {
        // 模拟服务行为
        when(feedbackService.getFeedbacksByStatus(SystemFeedback.FeedbackStatus.SUBMITTED))
                .thenReturn(Collections.singletonList(responseDTO));

        // 执行请求并验证结果
        mockMvc.perform(get("/api/feedback/status/SUBMITTED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responseDTO.getId()))
                .andExpect(jsonPath("$[0].status").value("SUBMITTED"));
    }
}
