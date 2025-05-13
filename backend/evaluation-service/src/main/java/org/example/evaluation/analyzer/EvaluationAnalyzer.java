package org.example.evaluation.analyzer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.evaluation.domain.ExperimentEvaluation;
import org.example.evaluation.dto.EvaluationStatisticsDTO;
import org.example.evaluation.repository.ExperimentEvaluationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评价数据分析器
 * 提供评价数据的高级分析功能
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EvaluationAnalyzer {

    private final ExperimentEvaluationRepository evaluationRepository;
    
    /**
     * 获取实验的评价统计数据
     * @param experimentId 实验ID
     * @return 统计数据
     */
    public EvaluationStatisticsDTO analyzeExperimentEvaluations(String experimentId) {
        log.info("Analyzing evaluations for experiment: {}", experimentId);
        
        // 获取实验的所有评价
        List<ExperimentEvaluation> evaluations = evaluationRepository.findByExperimentId(experimentId);
        
        // 计算平均评分
        Double averageRating = evaluationRepository.calculateAverageRatingByExperimentId(experimentId);
        
        // 统计各评分的数量
        Map<Integer, Long> ratingCounts = evaluations.stream()
                .collect(Collectors.groupingBy(ExperimentEvaluation::getRating, Collectors.counting()));
        
        // 构建统计DTO
        return EvaluationStatisticsDTO.builder()
                .experimentId(experimentId)
                .averageRating(averageRating != null ? averageRating : 0.0)
                .totalEvaluations(evaluations.size())
                .oneStarCount(ratingCounts.getOrDefault(1, 0L))
                .twoStarCount(ratingCounts.getOrDefault(2, 0L))
                .threeStarCount(ratingCounts.getOrDefault(3, 0L))
                .fourStarCount(ratingCounts.getOrDefault(4, 0L))
                .fiveStarCount(ratingCounts.getOrDefault(5, 0L))
                .build();
    }
    
    /**
     * 分析实验的评价趋势
     * 按时间段分析评价变化
     * @param experimentId 实验ID
     * @param periodDays 分析周期（天）
     * @return 各个周期的平均评分
     */
    public Map<String, Double> analyzeRatingTrend(String experimentId, int periodDays) {
        log.info("Analyzing rating trend for experiment: {} with period: {} days", experimentId, periodDays);
        
        // 获取实验的所有评价
        List<ExperimentEvaluation> evaluations = evaluationRepository.findByExperimentId(experimentId);
        
        // 按照创建时间分组，计算每个周期的平均评分
        // 这里简化处理，实际实现可能需要更复杂的时间处理
        return evaluations.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getCreatedAt().toLocalDate().toString(),
                        Collectors.averagingInt(ExperimentEvaluation::getRating)
                ));
    }
    
    /**
     * 分析用户评价行为
     * @param userId 用户ID
     * @return 用户的平均评分
     */
    public double analyzeUserRatingBehavior(String userId) {
        log.info("Analyzing rating behavior for user: {}", userId);
        
        // 获取用户的所有评价
        List<ExperimentEvaluation> evaluations = evaluationRepository.findByUserId(userId);
        
        if (evaluations.isEmpty()) {
            return 0.0;
        }
        
        // 计算用户的平均评分
        return evaluations.stream()
                .mapToInt(ExperimentEvaluation::getRating)
                .average()
                .orElse(0.0);
    }
    
    /**
     * 找出评分最高的实验
     * @param limit 返回的数量限制
     * @return 评分最高的实验ID列表
     */
    public List<String> findTopRatedExperiments(int limit) {
        log.info("Finding top {} rated experiments", limit);
        
        // 获取所有评价
        List<ExperimentEvaluation> allEvaluations = evaluationRepository.findAll();
        
        // 按实验ID分组，计算平均评分，并排序
        return allEvaluations.stream()
                .collect(Collectors.groupingBy(
                        ExperimentEvaluation::getExperimentId,
                        Collectors.averagingInt(ExperimentEvaluation::getRating)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
