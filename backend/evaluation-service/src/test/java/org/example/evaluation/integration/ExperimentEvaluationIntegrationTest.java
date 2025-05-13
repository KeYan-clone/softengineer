package org.example.evaluation.integration;

import org.example.evaluation.domain.CriteriaRating;
import org.example.evaluation.domain.ExperimentEvaluation;
import org.example.evaluation.dto.CriteriaRatingDTO;
import org.example.evaluation.dto.ExperimentEvaluationDTO;
import org.example.evaluation.dto.ExperimentEvaluationResponseDTO;
import org.example.evaluation.repository.ExperimentEvaluationRepository;
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

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 实验评价集成测试
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ExperimentEvaluationIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ExperimentEvaluationRepository evaluationRepository;

    private String baseUrl;
    private ExperimentEvaluationDTO evaluationDTO;
    private String experimentId = "test-exp-001";
    private String userId = "test-user-001";

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/evaluations/experiments";

        // 准备测试数据
        Set<CriteriaRatingDTO> criteriaRatings = new HashSet<>();
        criteriaRatings.add(new CriteriaRatingDTO("accuracy", 4));
        criteriaRatings.add(new CriteriaRatingDTO("usability", 5));

        evaluationDTO = new ExperimentEvaluationDTO();
        evaluationDTO.setExperimentId(experimentId);
        evaluationDTO.setUserId(userId);
        evaluationDTO.setRating(5);
        evaluationDTO.setComment("Integration test comment");
        evaluationDTO.setCriteriaRatings(criteriaRatings);
    }

    @AfterEach
    void cleanUp() {
        // 清理测试数据
        evaluationRepository.deleteAll();
    }

    @Test
    void testCreateAndGetEvaluation() {
        // 创建评价
        ResponseEntity<ExperimentEvaluationResponseDTO> createResponse = restTemplate
                .postForEntity(baseUrl, evaluationDTO, ExperimentEvaluationResponseDTO.class);

        // 验证创建成功
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getId());
        assertEquals(experimentId, createResponse.getBody().getExperimentId());
        assertEquals(userId, createResponse.getBody().getUserId());
        assertEquals(5, createResponse.getBody().getRating());
        assertEquals("Integration test comment", createResponse.getBody().getComment());
        assertEquals(2, createResponse.getBody().getCriteriaRatings().size());

        // 获取评价详情
        Long evaluationId = createResponse.getBody().getId();
        ResponseEntity<ExperimentEvaluationResponseDTO> getResponse = restTemplate
                .getForEntity(baseUrl + "/" + evaluationId, ExperimentEvaluationResponseDTO.class);

        // 验证获取成功
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(evaluationId, getResponse.getBody().getId());
        assertEquals(experimentId, getResponse.getBody().getExperimentId());
    }

    @Test
    void testUpdateEvaluation() {
        // 先创建评价
        ResponseEntity<ExperimentEvaluationResponseDTO> createResponse = restTemplate
                .postForEntity(baseUrl, evaluationDTO, ExperimentEvaluationResponseDTO.class);
        Long evaluationId = createResponse.getBody().getId();

        // 更新评价内容
        evaluationDTO.setRating(4);
        evaluationDTO.setComment("Updated integration test comment");

        ResponseEntity<ExperimentEvaluationResponseDTO> updateResponse = restTemplate
                .exchange(baseUrl + "/" + evaluationId, HttpMethod.PUT,
                        new HttpEntity<>(evaluationDTO), ExperimentEvaluationResponseDTO.class);

        // 验证更新成功
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals(evaluationId, updateResponse.getBody().getId());
        assertEquals(4, updateResponse.getBody().getRating());
        assertEquals("Updated integration test comment", updateResponse.getBody().getComment());
    }

    @Test
    void testDeleteEvaluation() {
        // 先创建评价
        ResponseEntity<ExperimentEvaluationResponseDTO> createResponse = restTemplate
                .postForEntity(baseUrl, evaluationDTO, ExperimentEvaluationResponseDTO.class);
        Long evaluationId = createResponse.getBody().getId();

        // 删除评价
        restTemplate.delete(baseUrl + "/" + evaluationId);

        // 验证删除成功
        ResponseEntity<ExperimentEvaluationResponseDTO> getResponse = restTemplate
                .getForEntity(baseUrl + "/" + evaluationId, ExperimentEvaluationResponseDTO.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void testGetEvaluationsByExperimentId() {
        // 先创建评价
        restTemplate.postForEntity(baseUrl, evaluationDTO, ExperimentEvaluationResponseDTO.class);

        // 获取指定实验的评价
        ResponseEntity<ExperimentEvaluationResponseDTO[]> getResponse = restTemplate
                .getForEntity(baseUrl + "?experimentId=" + experimentId, 
                        ExperimentEvaluationResponseDTO[].class);

        // 验证获取成功
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(1, getResponse.getBody().length);
        assertEquals(experimentId, getResponse.getBody()[0].getExperimentId());
    }

    @Test
    void testGetEvaluationStatistics() {
        // 先创建评价
        restTemplate.postForEntity(baseUrl, evaluationDTO, ExperimentEvaluationResponseDTO.class);

        // 获取统计数据
        ResponseEntity<Object> statsResponse = restTemplate
                .getForEntity(baseUrl + "/" + experimentId + "/statistics", Object.class);

        // 验证获取成功
        assertEquals(HttpStatus.OK, statsResponse.getStatusCode());
        assertNotNull(statsResponse.getBody());
    }
}
