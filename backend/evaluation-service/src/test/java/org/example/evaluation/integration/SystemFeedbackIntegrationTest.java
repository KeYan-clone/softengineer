package org.example.evaluation.integration;

import org.example.evaluation.domain.SystemFeedback;
import org.example.evaluation.dto.SystemFeedbackDTO;
import org.example.evaluation.dto.SystemFeedbackResponseDTO;
import org.example.evaluation.repository.SystemFeedbackRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 系统反馈集成测试
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SystemFeedbackIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SystemFeedbackRepository feedbackRepository;

    private String baseUrl;
    private SystemFeedbackDTO feedbackDTO;
    private String userId = "test-user-001";
    private String category = "interface";

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/feedback";

        // 准备测试数据
        feedbackDTO = new SystemFeedbackDTO();
        feedbackDTO.setUserId(userId);
        feedbackDTO.setCategory(category);
        feedbackDTO.setContent("Integration test feedback");
        feedbackDTO.setSuggestion("Integration test suggestion");
    }

    @AfterEach
    void cleanUp() {
        // 清理测试数据
        feedbackRepository.deleteAll();
    }

    @Test
    void testSubmitAndGetFeedback() {
        // 提交反馈
        ResponseEntity<SystemFeedbackResponseDTO> createResponse = restTemplate
                .postForEntity(baseUrl, feedbackDTO, SystemFeedbackResponseDTO.class);

        // 验证提交成功
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());
        assertEquals(userId, createResponse.getBody().getUserId());
        assertEquals(category, createResponse.getBody().getCategory());
        assertEquals("Integration test feedback", createResponse.getBody().getContent());
        assertEquals("Integration test suggestion", createResponse.getBody().getSuggestion());
        assertEquals(SystemFeedback.FeedbackStatus.SUBMITTED, createResponse.getBody().getStatus());

        // 获取反馈详情
        Long feedbackId = createResponse.getBody().getId();
        ResponseEntity<SystemFeedbackResponseDTO> getResponse = restTemplate
                .getForEntity(baseUrl + "/" + feedbackId, SystemFeedbackResponseDTO.class);

        // 验证获取成功
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(feedbackId, getResponse.getBody().getId());
        assertEquals(userId, getResponse.getBody().getUserId());
    }

    @Test
    void testUpdateFeedbackStatus() {
        // 先提交反馈
        ResponseEntity<SystemFeedbackResponseDTO> createResponse = restTemplate
                .postForEntity(baseUrl, feedbackDTO, SystemFeedbackResponseDTO.class);
        Long feedbackId = createResponse.getBody().getId();

        // 更新反馈状态
        String adminResponse = "We are looking into this issue";
        String statusUpdateUrl = UriComponentsBuilder.fromUriString(baseUrl + "/" + feedbackId + "/status")
                .queryParam("status", SystemFeedback.FeedbackStatus.PROCESSING)
                .queryParam("adminResponse", adminResponse)
                .toUriString();

        ResponseEntity<SystemFeedbackResponseDTO> updateResponse = restTemplate
                .exchange(statusUpdateUrl, HttpMethod.PUT, 
                        null, SystemFeedbackResponseDTO.class);

        // 验证更新成功
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals(feedbackId, updateResponse.getBody().getId());
        assertEquals(SystemFeedback.FeedbackStatus.PROCESSING, updateResponse.getBody().getStatus());
        assertEquals(adminResponse, updateResponse.getBody().getAdminResponse());
    }

    @Test
    void testDeleteFeedback() {
        // 先提交反馈
        ResponseEntity<SystemFeedbackResponseDTO> createResponse = restTemplate
                .postForEntity(baseUrl, feedbackDTO, SystemFeedbackResponseDTO.class);
        Long feedbackId = createResponse.getBody().getId();

        // 删除反馈
        restTemplate.delete(baseUrl + "/" + feedbackId);

        // 验证删除成功
        ResponseEntity<SystemFeedbackResponseDTO> getResponse = restTemplate
                .getForEntity(baseUrl + "/" + feedbackId, SystemFeedbackResponseDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testGetFeedbacksByUserId() {
        // 先提交反馈
        restTemplate.postForEntity(baseUrl, feedbackDTO, SystemFeedbackResponseDTO.class);

        // 获取指定用户的反馈
        ResponseEntity<SystemFeedbackResponseDTO[]> getResponse = restTemplate
                .getForEntity(baseUrl + "/user/" + userId, 
                        SystemFeedbackResponseDTO[].class);

        // 验证获取成功
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(1, getResponse.getBody().length);
        assertEquals(userId, getResponse.getBody()[0].getUserId());
    }

    @Test
    void testGetFeedbacksByCategory() {
        // 先提交反馈
        restTemplate.postForEntity(baseUrl, feedbackDTO, SystemFeedbackResponseDTO.class);

        // 获取指定类别的反馈
        ResponseEntity<SystemFeedbackResponseDTO[]> getResponse = restTemplate
                .getForEntity(baseUrl + "/category/" + category, 
                        SystemFeedbackResponseDTO[].class);

        // 验证获取成功
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(1, getResponse.getBody().length);
        assertEquals(category, getResponse.getBody()[0].getCategory());
    }
}
