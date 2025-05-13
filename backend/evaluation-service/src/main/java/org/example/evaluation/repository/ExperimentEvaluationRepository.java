package org.example.evaluation.repository;

import org.example.evaluation.domain.ExperimentEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 实验评价数据访问层
 */
@Repository
public interface ExperimentEvaluationRepository extends JpaRepository<ExperimentEvaluation, Long> {

    /**
     * 根据实验ID查找所有评价
     * @param experimentId 实验ID
     * @return 评价列表
     */
    List<ExperimentEvaluation> findByExperimentId(String experimentId);
    
    /**
     * 根据实验ID分页查找评价
     * @param experimentId 实验ID
     * @param pageable 分页参数
     * @return 分页评价结果
     */
    Page<ExperimentEvaluation> findByExperimentId(String experimentId, Pageable pageable);
    
    /**
     * 查找用户对指定实验的评价
     * @param experimentId 实验ID
     * @param userId 用户ID
     * @return 评价（如存在）
     */
    Optional<ExperimentEvaluation> findByExperimentIdAndUserId(String experimentId, String userId);
    
    /**
     * 根据用户ID查找该用户的所有评价
     * @param userId 用户ID
     * @return 评价列表
     */
    List<ExperimentEvaluation> findByUserId(String userId);
    
    /**
     * 根据用户ID分页查找评价
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 分页评价结果
     */
    Page<ExperimentEvaluation> findByUserId(String userId, Pageable pageable);
    
    /**
     * 根据实验ID删除所有评价
     * @param experimentId 实验ID
     * @return 删除的记录数
     */
    long deleteByExperimentId(String experimentId);
    
    /**
     * 根据用户ID删除所有评价
     * @param userId 用户ID
     * @return 删除的记录数
     */
    long deleteByUserId(String userId);
    
    /**
     * 计算指定实验的平均评分
     * @param experimentId 实验ID
     * @return 平均评分
     */
    @Query("SELECT AVG(e.rating) FROM ExperimentEvaluation e WHERE e.experimentId = :experimentId")
    Double calculateAverageRatingByExperimentId(@Param("experimentId") String experimentId);
    
    /**
     * 统计指定实验的评价数量
     * @param experimentId 实验ID
     * @return 评价数量
     */
    long countByExperimentId(String experimentId);
}
