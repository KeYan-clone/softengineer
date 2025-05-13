package org.example.evaluation.performance;

import org.example.evaluation.domain.ExperimentEvaluation;
import org.example.evaluation.dto.ExperimentEvaluationDTO;
import org.example.evaluation.dto.ExperimentEvaluationResponseDTO;
import org.example.evaluation.repository.ExperimentEvaluationRepository;
import org.example.evaluation.service.ExperimentEvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 评价服务性能测试
 * 注意: 这些测试针对性能，可能需要更长时间运行
 */
@SpringBootTest
@ActiveProfiles("test")
@Tag("performance")
public class EvaluationServicePerformanceTest {

    @Autowired
    private ExperimentEvaluationService evaluationService;

    @Autowired
    private ExperimentEvaluationRepository evaluationRepository;

    private List<ExperimentEvaluationDTO> testData;
    private final int BATCH_SIZE = 100;
    private final int CONCURRENT_USERS = 10;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testData = new ArrayList<>();
        for (int i = 0; i < BATCH_SIZE; i++) {
            ExperimentEvaluationDTO dto = new ExperimentEvaluationDTO();
            dto.setExperimentId("perf-exp-" + (i % 10));
            dto.setUserId("perf-user-" + i);
            dto.setRating(i % 5 + 1);
            dto.setComment("Performance test comment " + i);
            dto.setCriteriaRatings(new HashSet<>());
            testData.add(dto);
        }
    }

    @Test
    @Transactional
    void testBatchEvaluationCreation() {
        // 批量创建评价，测量总耗时
        long startTime = System.currentTimeMillis();

        List<ExperimentEvaluationResponseDTO> responses = new ArrayList<>();
        for (ExperimentEvaluationDTO dto : testData) {
            responses.add(evaluationService.createEvaluation(dto));
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // 验证结果
        assertEquals(BATCH_SIZE, responses.size());
        System.out.println("创建 " + BATCH_SIZE + " 条评价记录耗时: " + duration + " ms");
        System.out.println("平均每条记录耗时: " + (duration / (float) BATCH_SIZE) + " ms");
        
        // 清理测试数据
        evaluationRepository.deleteAllById(responses.stream().map(ExperimentEvaluationResponseDTO::getId).toList());
    }

    @Test
    void testConcurrentEvaluationCreationAndRetrieval() throws Exception {
        // 使用线程池模拟并发用户
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_USERS);
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        // 每个线程创建和检索评价
        for (int i = 0; i < CONCURRENT_USERS; i++) {
            final int userId = i;
            CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
                try {
                    // 创建评价
                    ExperimentEvaluationDTO dto = new ExperimentEvaluationDTO();
                    dto.setExperimentId("concurrent-exp");
                    dto.setUserId("concurrent-user-" + userId);
                    dto.setRating(4);
                    dto.setComment("Concurrent test");
                    dto.setCriteriaRatings(new HashSet<>());

                    ExperimentEvaluationResponseDTO created = evaluationService.createEvaluation(dto);
                    
                    // 检索并验证
                    ExperimentEvaluationResponseDTO retrieved = evaluationService.getEvaluation(created.getId());
                    return created.getId().equals(retrieved.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }, executor);
            
            futures.add(future);
        }

        // 等待所有任务完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));
        
        allFutures.get(30, TimeUnit.SECONDS);  // 设置超时时间

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // 验证所有操作成功
        for (CompletableFuture<Boolean> future : futures) {
            assertTrue(future.get());
        }

        System.out.println(CONCURRENT_USERS + " 个并发用户创建和检索评价耗时: " + duration + " ms");
        System.out.println("平均每个用户操作耗时: " + (duration / (float) CONCURRENT_USERS) + " ms");

        // 关闭线程池
        executor.shutdown();
        
        // 清理测试数据
        evaluationRepository.deleteByExperimentId("concurrent-exp");
    }
}
