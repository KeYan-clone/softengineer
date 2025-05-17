package org.linghu.mybackend.service;

import org.linghu.mybackend.domain.Discussion;
import org.linghu.mybackend.dto.DiscussionRequestDTO;
import org.linghu.mybackend.dto.DiscussionResponseDTO;
import org.linghu.mybackend.dto.PriorityRequestDTO;
import org.linghu.mybackend.dto.ReviewRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 讨论服务接口
 * 定义与讨论相关的业务操作，包括创建、查询、更新、删除、审核、点赞等功能
 */
public interface DiscussionService {
    
    /**
     * 创建讨论
     * 
     * @param requestDTO 创建讨论请求
     * @param userId     创建讨论用户id
     * @return 创建的讨论对象
     */
    DiscussionResponseDTO createDiscussion(DiscussionRequestDTO requestDTO, String userId);
    
    /**
     * 获取讨论列表
     * 支持多种过滤条件和排序方式的分页查询
     * 
     * @param tags 标签过滤(数组)
     * @param experimentId 实验ID过滤
     * @param userId 用户ID过滤
     * @param status 审核状态过滤(PENDING,APPROVED,REJECTED)
     * @param keyword 关键词搜索
     * @param sortBy 排序字段(createTime,lastActivityTime,likeCount,commentCount,viewCount)
     * @param order 排序方向(asc,desc)
     * @param page 页码(从0开始)
     * @param size 每页大小
     * @param currentUserId 当前用户ID(用于判断是否点赞)
     * @return 分页的讨论列表
     */
    Page<DiscussionResponseDTO> getDiscussions(
            String[] tags, 
            String experimentId, 
            String userId, 
            String status, 
            String keyword, 
            String sortBy, 
            String order, 
            int page, 
            int size,            String currentUserId);
    
    /**
     * 根据ID获取讨论详情
     * 
     * @param id 讨论ID
     * @param currentUserId 当前用户ID(用于判断是否点赞)
     * @return 讨论详情
     */
    DiscussionResponseDTO getDiscussionById(String id, String currentUserId);
    
    /**
     * 更新讨论内容
     * 
     * @param id 讨论ID
     * @param requestDTO 更新请求
     * @param userId 操作用户ID(用于权限验证)
     * @return 更新后的讨论对象
     */
    DiscussionResponseDTO updateDiscussion(String id, DiscussionRequestDTO requestDTO, String userId);
    
    /**
     * 删除讨论
     * 
     * @param id 讨论ID
     * @param userId 操作用户ID(用于权限验证)
     */
    boolean deleteDiscussion(String id, String userId);
    
    /**
     * 审核讨论
     * 可以将讨论设置为已批准或已拒绝状态
     * 
     * @param id 讨论ID
     * @param requestDTO 审核请求(包含状态和拒绝理由)
     * @param reviewerId 审核人ID
     * @return 审核后的讨论对象
     */
    DiscussionResponseDTO reviewDiscussion(String id, ReviewRequestDTO requestDTO, String reviewerId);
    
    /**
     * 更新讨论优先级
     * 用于设置讨论置顶等特殊展示
     * 
     * @param id 讨论ID
     * @param requestDTO 优先级请求
     * @param userId 操作用户ID(用于权限验证)
     * @return 更新后的讨论对象
     */
    DiscussionResponseDTO updatePriority(String id, PriorityRequestDTO requestDTO, String userId);
    
    /**
     * 点赞/取消点赞讨论
     * 
     * @param id 讨论ID
     * @param userId 操作用户ID
     * @return 更新后的讨论对象
     */
    DiscussionResponseDTO toggleLike(String id, String userId);
    
    /**
     * 增加讨论的浏览次数
     * 
     * @param id 讨论ID
     */
    void incrementViewCount(String id);
}
