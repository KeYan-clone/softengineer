package org.example.evaluation.analyzer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.evaluation.domain.SystemFeedback;
import org.example.evaluation.repository.SystemFeedbackRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 反馈数据分析器
 * 提供系统反馈数据的分析功能
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FeedbackAnalyzer {

    private final SystemFeedbackRepository feedbackRepository;
    
    /**
     * 分析反馈类别分布
     * @return 各类别的反馈数量
     */
    public Map<String, Long> analyzeFeedbackCategoryDistribution() {
        log.info("Analyzing feedback category distribution");
        
        // 获取所有反馈
        List<SystemFeedback> allFeedback = feedbackRepository.findAll();
        
        // 按类别分组，统计数量
        return allFeedback.stream()
                .collect(Collectors.groupingBy(
                        SystemFeedback::getCategory,
                        Collectors.counting()
                ));
    }
    
    /**
     * 分析反馈状态分布
     * @return 各状态的反馈数量
     */
    public Map<SystemFeedback.FeedbackStatus, Long> analyzeFeedbackStatusDistribution() {
        log.info("Analyzing feedback status distribution");
        
        // 获取所有反馈
        List<SystemFeedback> allFeedback = feedbackRepository.findAll();
        
        // 按状态分组，统计数量
        return allFeedback.stream()
                .collect(Collectors.groupingBy(
                        SystemFeedback::getStatus,
                        Collectors.counting()
                ));
    }
    
    /**
     * 分析反馈时间分布
     * @param days 天数
     * @return 指定天数内每天的反馈数量
     */
    public Map<String, Long> analyzeFeedbackTimeDistribution(int days) {
        log.info("Analyzing feedback time distribution for the last {} days", days);
        
        // 计算起始时间
        LocalDateTime startTime = LocalDateTime.now().minusDays(days);
        
        // 获取时间范围内的反馈
        List<SystemFeedback> feedbacks = feedbackRepository.findByCreatedAtBetween(startTime, LocalDateTime.now());
        
        // 按日期分组，统计数量
        return feedbacks.stream()
                .collect(Collectors.groupingBy(
                        feedback -> feedback.getCreatedAt().toLocalDate().toString(),
                        Collectors.counting()
                ));
    }
    
    /**
     * 分析反馈响应时间
     * @return 平均响应时间（小时）
     */
    public double analyzeAverageResponseTime() {
        log.info("Analyzing average feedback response time");
        
        // 获取所有已处理的反馈
        List<SystemFeedback> resolvedFeedback = feedbackRepository.findByStatus(SystemFeedback.FeedbackStatus.RESOLVED);
        
        if (resolvedFeedback.isEmpty()) {
            return 0.0;
        }
        
        // 计算创建时间到更新时间的平均小时差
        return resolvedFeedback.stream()
                .mapToDouble(feedback -> {
                    LocalDateTime createdAt = feedback.getCreatedAt();
                    LocalDateTime updatedAt = feedback.getUpdatedAt();
                    return (updatedAt.toEpochSecond(java.time.ZoneOffset.UTC) - 
                            createdAt.toEpochSecond(java.time.ZoneOffset.UTC)) / 3600.0;
                })
                .average()
                .orElse(0.0);
    }
    
    /**
     * 找出常见问题类别
     * @param limit 返回的数量限制
     * @return 最常见的反馈类别
     */
    public List<String> findTopFeedbackCategories(int limit) {
        log.info("Finding top {} feedback categories", limit);
        
        // 获取所有反馈
        List<SystemFeedback> allFeedback = feedbackRepository.findAll();
        
        // 按类别分组，统计数量，并排序
        return allFeedback.stream()
                .collect(Collectors.groupingBy(
                        SystemFeedback::getCategory,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
