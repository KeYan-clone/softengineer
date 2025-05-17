package org.linghu.mybackend.service;

import org.linghu.mybackend.domain.Comment;
import org.linghu.mybackend.dto.CommentRequestDTO;
import org.linghu.mybackend.dto.CommentResponseDTO;
import org.linghu.mybackend.dto.ReportCommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 评论服务接口
 * 定义与评论相关的业务操作，包括创建、查询、回复、删除、点赞、举报等功能
 */
public interface CommentService {
    
    /**
     * 创建评论
     * 支持对讨论的评论以及对评论的回复
     * 
     * @param discussionId 讨论ID
     * @param requestDTO 创建评论请求
     * @param userId 创建评论用户ID
     * @return 创建的评论对象
     */
    CommentResponseDTO createComment(String discussionId, CommentRequestDTO requestDTO, String userId);
    
    /**
     * 获取讨论的评论列表
     * 支持只获取根评论或获取所有评论
     * 
     * @param discussionId 讨论ID
     * @param rootOnly 是否只返回根评论
     * @param sortBy 排序字段(createTime,likeCount)
     * @param order 排序方向(asc,desc)
     * @param page 页码(从0开始)
     * @param size 每页大小
     * @param currentUserId 当前用户ID(用于判断是否点赞)
     * @return 分页的评论列表
     */
    Page<CommentResponseDTO> getCommentsByDiscussionId(
            String discussionId, 
            boolean rootOnly, 
            String sortBy, 
            String order, 
            int page, 
            int size,
            String currentUserId);
      /**
     * 获取评论的回复列表
     * 
     * @param commentId 评论ID
     * @param page 页码(从0开始)
     * @param size 每页大小
     * @param currentUserId 当前用户ID(用于判断是否点赞)
     * @return 分页的回复列表
     */
    Page<CommentResponseDTO> getRepliesByCommentId(
            String commentId, 
            int page, 
            int size,
            String currentUserId);
    
    /**
     * 删除评论
     * 仅评论创建者可删除
     * 
     * @param commentId 评论ID
     * @param userId 操作用户ID(用于权限验证)
     */
    void deleteComment(String commentId, String userId);
    
    /**
     * 点赞/取消点赞评论
     * 
     * @param commentId 评论ID
     * @param userId 操作用户ID
     * @return 更新后的评论对象
     */
    CommentResponseDTO toggleLike(String commentId, String userId);
    
    /**
     * 根据ID获取评论详情
     * 
     * @param commentId 评论ID
     * @param currentUserId 当前用户ID(用于判断是否点赞)
     * @return 评论详情
     */
    CommentResponseDTO getCommentById(String commentId, String currentUserId);
    
    /**
     * 举报评论
     * 将评论标记为需要审核状态
     * 
     * @param commentId 评论ID
     * @param reportDTO 举报请求(包含举报原因和详情)
     * @param userId 举报用户ID
     */
    void reportComment(String commentId, ReportCommentDTO reportDTO, String userId);
}
