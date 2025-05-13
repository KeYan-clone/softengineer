package org.example.evaluation.service;

import org.example.evaluation.domain.SystemFeedback;
import org.example.evaluation.dto.SystemFeedbackDTO;
import org.example.evaluation.dto.SystemFeedbackResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 系统反馈服务接口
 */
public interface SystemFeedbackService {

    /**
     * 提交系统反馈
     * @param feedbackDTO 反馈数据
     * @return 创建的反馈
     */
    SystemFeedbackResponseDTO submitFeedback(SystemFeedbackDTO feedbackDTO);
    
    /**
     * 获取系统反馈
     * @param id 反馈ID
     * @return 反馈数据
     */
    SystemFeedbackResponseDTO getFeedback(Long id);
    
    /**
     * 更新反馈状态
     * @param id 反馈ID
     * @param status 新状态
     * @param adminResponse 管理员回复
     * @return 更新后的反馈
     */
    SystemFeedbackResponseDTO updateFeedbackStatus(Long id, SystemFeedback.FeedbackStatus status, String adminResponse);
    
    /**
     * 删除系统反馈
     * @param id 反馈ID
     */
    void deleteFeedback(Long id);
    
    /**
     * 获取指定用户的所有反馈
     * @param userId 用户ID
     * @return 反馈列表
     */
    List<SystemFeedbackResponseDTO> getFeedbacksByUserId(String userId);
    
    /**
     * 分页获取指定用户的反馈
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 分页反馈结果
     */
    Page<SystemFeedbackResponseDTO> getFeedbacksByUserId(String userId, Pageable pageable);
    
    /**
     * 根据类别获取反馈
     * @param category 类别
     * @return 反馈列表
     */
    List<SystemFeedbackResponseDTO> getFeedbacksByCategory(String category);
    
    /**
     * 分页获取指定类别的反馈
     * @param category 类别
     * @param pageable 分页参数
     * @return 分页反馈结果
     */
    Page<SystemFeedbackResponseDTO> getFeedbacksByCategory(String category, Pageable pageable);
    
    /**
     * 根据状态获取反馈
     * @param status 状态
     * @return 反馈列表
     */
    List<SystemFeedbackResponseDTO> getFeedbacksByStatus(SystemFeedback.FeedbackStatus status);
    
    /**
     * 分页获取指定状态的反馈
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页反馈结果
     */
    Page<SystemFeedbackResponseDTO> getFeedbacksByStatus(SystemFeedback.FeedbackStatus status, Pageable pageable);
}
