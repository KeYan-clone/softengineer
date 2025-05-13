package org.example.evaluation.repository;

import org.example.evaluation.domain.SystemFeedback;
import org.example.evaluation.domain.SystemFeedback.FeedbackStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统反馈数据访问层
 */
@Repository
public interface SystemFeedbackRepository extends JpaRepository<SystemFeedback, Long> {

    /**
     * 根据用户ID查找该用户的所有反馈
     * @param userId 用户ID
     * @return 反馈列表
     */
    List<SystemFeedback> findByUserId(String userId);
    
    /**
     * 根据用户ID分页查找反馈
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 分页反馈结果
     */
    Page<SystemFeedback> findByUserId(String userId, Pageable pageable);
    
    /**
     * 根据分类查找反馈
     * @param category 分类
     * @return 反馈列表
     */
    List<SystemFeedback> findByCategory(String category);
    
    /**
     * 根据分类分页查找反馈
     * @param category 分类
     * @param pageable 分页参数
     * @return 分页反馈结果
     */
    Page<SystemFeedback> findByCategory(String category, Pageable pageable);
    
    /**
     * 根据状态查找反馈
     * @param status 反馈状态
     * @return 反馈列表
     */
    List<SystemFeedback> findByStatus(FeedbackStatus status);
    
    /**
     * 根据状态分页查找反馈
     * @param status 反馈状态
     * @param pageable 分页参数
     * @return 分页反馈结果
     */
    Page<SystemFeedback> findByStatus(FeedbackStatus status, Pageable pageable);
    
    /**
     * 根据创建时间范围查找反馈
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 反馈列表
     */
    List<SystemFeedback> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根据创建时间范围分页查找反馈
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageable 分页参数
     * @return 分页反馈结果
     */
    Page<SystemFeedback> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
    
    /**
     * 统计指定状态的反馈数量
     * @param status 反馈状态
     * @return 反馈数量
     */
    long countByStatus(FeedbackStatus status);
}
