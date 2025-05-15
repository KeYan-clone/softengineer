package org.linghu.mybackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.linghu.mybackend.domain.Attachment;
import org.linghu.mybackend.domain.Comment;
import org.linghu.mybackend.domain.Discussion;
import org.linghu.mybackend.domain.RichContent;
import org.linghu.mybackend.dto.AttachmentDTO;
import org.linghu.mybackend.dto.CommentRequestDTO;
import org.linghu.mybackend.dto.CommentResponseDTO;
import org.linghu.mybackend.dto.ReportCommentDTO;
import org.linghu.mybackend.dto.RichContentDTO;
import org.linghu.mybackend.dto.UserDTO;
import org.linghu.mybackend.exception.ResourceNotFoundException;
import org.linghu.mybackend.exception.UnauthorizedException;
import org.linghu.mybackend.repository.CommentRepository;
import org.linghu.mybackend.repository.DiscussionRepository;
import org.linghu.mybackend.service.CommentService;
import org.linghu.mybackend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    
    private final CommentRepository commentRepository;
    private final DiscussionRepository discussionRepository;
    private final UserService userService;
    
    @Override
    public CommentResponseDTO createComment(String discussionId, CommentRequestDTO requestDTO, String userId) {        // 检查discussion是否存在
        Discussion discussion = discussionRepository.findByIdAndNotDeleted(discussionId)
                .orElseThrow(() -> new ResourceNotFoundException("Discussion not found with id: " + discussionId));
        
        UserDTO userInfo = userService.getUserInfo(userId);
        
        // 创建评论实体
        Comment comment = Comment.builder()
                .discussionId(discussionId)
                .content(requestDTO.getContent())
                .richContent(RichContent.builder()
                        .html(requestDTO.getRichContent() != null ? requestDTO.getRichContent().getHtml() : null)
                        .delta(requestDTO.getRichContent() != null ? requestDTO.getRichContent().getDelta() : null)
                        .build())
                .userId(userId)
                .username(userInfo.getUsername())
                .userAvatar(userInfo.getAvatar())
                .parentId(requestDTO.getParentId())
                .likeCount(0)
                .likedBy(new ArrayList<>())
                .attachments(mapAttachmentDTOsToAttachments(requestDTO.getAttachments()))
                .status("VISIBLE")
                .deleted(false)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        
        // 处理回复关系
        if (requestDTO.getParentId() != null && !requestDTO.getParentId().isEmpty()) {
            Comment parentComment = commentRepository.findByIdAndNotDeleted(requestDTO.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found with id: " + requestDTO.getParentId()));
              if (requestDTO.getReplyToUserId() != null && !requestDTO.getReplyToUserId().isEmpty()) {
                UserDTO replyToUser = userService.getUserInfo(requestDTO.getReplyToUserId());
                comment.setReplyToUserId(requestDTO.getReplyToUserId());
                comment.setReplyToUsername(replyToUser.getUsername());
            }
            
            // 设置根评论ID和路径
            if (parentComment.getRootId() != null) {
                // 如果父评论已经是某个评论的回复
                comment.setRootId(parentComment.getRootId());
                comment.setPath(parentComment.getPath() + "." + UUID.randomUUID().toString().replace("-", "").substring(0, 8));
                comment.setDepth(parentComment.getDepth() + 1);
            } else {
                // 如果父评论是根评论
                comment.setRootId(parentComment.getId());
                comment.setPath(parentComment.getId() + "." + UUID.randomUUID().toString().replace("-", "").substring(0, 8));
                comment.setDepth(1);
            }
        } else {
            // 根评论
            comment.setDepth(0);
            // path将在保存后设置
        }
        
        Comment savedComment = commentRepository.save(comment);
        
        // 如果是根评论，设置path为自己的ID
        if (comment.getDepth() == 0) {
            savedComment.setPath(savedComment.getId());
            savedComment = commentRepository.save(savedComment);
        }
        
        // 更新discussion的评论计数和最后评论时间
        discussion.setCommentCount(discussion.getCommentCount() + 1);
        discussion.setLastCommentTime(LocalDateTime.now());
        discussion.setLastActivityTime(LocalDateTime.now());
        discussionRepository.save(discussion);
        
        return mapCommentToResponseDTO(savedComment, userId);
    }
    
    @Override
    public Page<CommentResponseDTO> getCommentsByDiscussionId(
            String discussionId, 
            boolean rootOnly, 
            String sortBy, 
            String order, 
            int page, 
            int size,
            String currentUserId) {
        
        // 验证discussion存在
        discussionRepository.findByIdAndNotDeleted(discussionId)
                .orElseThrow(() -> new ResourceNotFoundException("Discussion not found with id: " + discussionId));
        
        Sort sort = createSort(sortBy, order);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Comment> comments;
        if (rootOnly) {
            // 只获取根评论
            comments = commentRepository.findRootCommentsByDiscussionId(discussionId, pageable);
            
            // 转换为DTO并加载回复
            return comments.map(comment -> {
                CommentResponseDTO dto = mapCommentToResponseDTO(comment, currentUserId);
                List<Comment> replies = commentRepository.findByRootId(comment.getId());
                dto.setReplies(replies.stream()
                        .filter(reply -> !reply.getId().equals(comment.getId())) // 排除自身
                        .map(reply -> mapCommentToResponseDTO(reply, currentUserId))
                        .collect(Collectors.toList()));
                return dto;
            });
        } else {
            // 获取所有评论
            comments = commentRepository.findByDiscussionId(discussionId, pageable);
            return comments.map(comment -> mapCommentToResponseDTO(comment, currentUserId));
        }
    }
    
    @Override
    public Page<CommentResponseDTO> getRepliesByCommentId(
            String commentId, 
            int page, 
            int size,
            String currentUserId) {
        
        // 验证评论存在
        Comment comment = commentRepository.findByIdAndNotDeleted(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").ascending());
        Page<Comment> replies = commentRepository.findByParentId(commentId, pageable);
        
        return replies.map(reply -> mapCommentToResponseDTO(reply, currentUserId));
    }
    
    @Override
    public void deleteComment(String commentId, String userId) {
        Comment comment = commentRepository.findByIdAndNotDeleted(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        
        // 验证是否是创建者
        if (!comment.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this comment");
        }
        
        // 软删除
        comment.setDeleted(true);
        comment.setUpdateTime(LocalDateTime.now());
        commentRepository.save(comment);
        
        // 更新discussion的评论计数
        Discussion discussion = discussionRepository.findByIdAndNotDeleted(comment.getDiscussionId())
                .orElseThrow(() -> new ResourceNotFoundException("Discussion not found with id: " + comment.getDiscussionId()));
        
        long commentCount = commentRepository.countByDiscussionIdAndDeletedFalse(comment.getDiscussionId());
        discussion.setCommentCount(commentCount);
        discussionRepository.save(discussion);
    }
    
    @Override
    public CommentResponseDTO toggleLike(String commentId, String userId) {
        Comment comment = commentRepository.findByIdAndNotDeleted(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        
        List<String> likedBy = comment.getLikedBy();
        
        if (likedBy.contains(userId)) {
            // 取消点赞
            likedBy.remove(userId);
            comment.setLikeCount(comment.getLikeCount() - 1);
        } else {
            // 添加点赞
            likedBy.add(userId);
            comment.setLikeCount(comment.getLikeCount() + 1);
        }
        
        comment.setLikedBy(likedBy);
        comment.setUpdateTime(LocalDateTime.now());
        
        Comment updatedComment = commentRepository.save(comment);
        return mapCommentToResponseDTO(updatedComment, userId);
    }
    
    @Override
    public CommentResponseDTO getCommentById(String commentId, String currentUserId) {
        Comment comment = commentRepository.findByIdAndNotDeleted(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        
        return mapCommentToResponseDTO(comment, currentUserId);
    }
    
    @Override
    public void reportComment(String commentId, ReportCommentDTO reportDTO, String userId) {
        Comment comment = commentRepository.findByIdAndNotDeleted(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        
        // 设置为已举报状态
        comment.setStatus("FLAGGED");
        comment.setUpdateTime(LocalDateTime.now());
        
        // TODO: 保存举报信息到单独的举报表中
        
        commentRepository.save(comment);
    }
    
    // 辅助方法
    
    private Sort createSort(String sortBy, String order) {
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "createTime";  // 默认按创建时间排序
        }
        
        if (order == null || order.isEmpty()) {
            order = "asc";  // 默认升序
        }
        
        // 映射API参数到字段名称
        String fieldName;
        switch (sortBy) {
            case "createTime":
                fieldName = "createTime";
                break;
            case "likeCount":
                fieldName = "likeCount";
                break;
            default:
                fieldName = "createTime";
        }
        
        // 创建排序
        if ("asc".equals(order.toLowerCase())) {
            return Sort.by(fieldName).ascending();
        } else {
            return Sort.by(fieldName).descending();
        }
    }
    
    private List<Attachment> mapAttachmentDTOsToAttachments(List<AttachmentDTO> attachmentDTOs) {
        if (attachmentDTOs == null) {
            return new ArrayList<>();
        }
        
        return attachmentDTOs.stream()
                .map(dto -> Attachment.builder()
                        .url(dto.getUrl())
                        .type(dto.getType())
                        .name(dto.getName())
                        .size(dto.getSize())
                        .thumbnailUrl(dto.getThumbnailUrl())
                        .build())
                .collect(Collectors.toList());
    }
    
    private List<AttachmentDTO> mapAttachmentsToAttachmentDTOs(List<Attachment> attachments) {
        if (attachments == null) {
            return new ArrayList<>();
        }
        
        return attachments.stream()
                .map(attachment -> AttachmentDTO.builder()
                        .url(attachment.getUrl())
                        .type(attachment.getType())
                        .name(attachment.getName())
                        .size(attachment.getSize())
                        .thumbnailUrl(attachment.getThumbnailUrl())
                        .build())
                .collect(Collectors.toList());
    }
    
    private CommentResponseDTO mapCommentToResponseDTO(Comment comment, String currentUserId) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .discussionId(comment.getDiscussionId())
                .content(comment.getContent())
                .richContent(comment.getRichContent() != null ? 
                        RichContentDTO.builder()
                                .html(comment.getRichContent().getHtml())
                                .delta(comment.getRichContent().getDelta())
                                .build() : null)
                .userId(comment.getUserId())
                .username(comment.getUsername())
                .userAvatar(comment.getUserAvatar())
                .parentId(comment.getParentId())
                .rootId(comment.getRootId())
                .path(comment.getPath())
                .depth(comment.getDepth())
                .replyToUserId(comment.getReplyToUserId())
                .replyToUsername(comment.getReplyToUsername())
                .likeCount(comment.getLikeCount())
                .isLiked(comment.getLikedBy() != null && comment.getLikedBy().contains(currentUserId))
                .attachments(mapAttachmentsToAttachmentDTOs(comment.getAttachments()))
                .status(comment.getStatus())
                .createTime(comment.getCreateTime())
                .updateTime(comment.getUpdateTime())
                .build();
    }
}
