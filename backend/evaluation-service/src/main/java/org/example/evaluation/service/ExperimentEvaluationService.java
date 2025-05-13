package org.example.evaluation.service;

import org.example.evaluation.dto.ExperimentEvaluationDTO;
import org.example.evaluation.dto.ExperimentEvaluationResponseDTO;
import org.example.evaluation.dto.EvaluationStatisticsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 实验评价服务接口
 */
public interface ExperimentEvaluationService {

    /**
     * 创建实验评价
     * @param evaluationDTO 评价数据
     * @return 创建的评价
     */
    ExperimentEvaluationResponseDTO createEvaluation(ExperimentEvaluationDTO evaluationDTO);
    
    /**
     * 更新实验评价
     * @param id 评价ID
     * @param evaluationDTO 评价数据
     * @return 更新后的评价
     */
    ExperimentEvaluationResponseDTO updateEvaluation(Long id, ExperimentEvaluationDTO evaluationDTO);
    
    /**
     * 获取实验评价
     * @param id 评价ID
     * @return 评价数据
     */
    ExperimentEvaluationResponseDTO getEvaluation(Long id);
    
    /**
     * 删除实验评价
     * @param id 评价ID
     */
    void deleteEvaluation(Long id);
    
    /**
     * 获取指定实验的所有评价
     * @param experimentId 实验ID
     * @return 评价列表
     */
    List<ExperimentEvaluationResponseDTO> getEvaluationsByExperimentId(String experimentId);
    
    /**
     * 分页获取指定实验的评价
     * @param experimentId 实验ID
     * @param pageable 分页参数
     * @return 分页评价结果
     */
    Page<ExperimentEvaluationResponseDTO> getEvaluationsByExperimentId(String experimentId, Pageable pageable);
    
    /**
     * 获取指定用户的所有评价
     * @param userId 用户ID
     * @return 评价列表
     */
    List<ExperimentEvaluationResponseDTO> getEvaluationsByUserId(String userId);
    
    /**
     * 分页获取指定用户的评价
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 分页评价结果
     */
    Page<ExperimentEvaluationResponseDTO> getEvaluationsByUserId(String userId, Pageable pageable);
    
    /**
     * 获取用户对指定实验的评价
     * @param experimentId 实验ID
     * @param userId 用户ID
     * @return 评价数据，如果不存在则返回null
     */
    ExperimentEvaluationResponseDTO getUserExperimentEvaluation(String experimentId, String userId);
    
    /**
     * 获取实验评价统计数据
     * @param experimentId 实验ID
     * @return 统计数据
     */
    EvaluationStatisticsDTO getEvaluationStatistics(String experimentId);
}
