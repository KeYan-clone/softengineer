package org.example.discussion.service;

import lombok.RequiredArgsConstructor;
import org.example.discussion.domain.Comment;
import org.example.discussion.dto.CommentCreateDTO;
import org.example.discussion.dto.CommentDTO;
import org.example.discussion.dto.UserDTO;
import org.example.discussion.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for comment-related operations
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final DiscussionService discussionService;
    private final NotificationService notificationService;
    
    /**
     * Create a new comment
     *
     * @param createDTO Comment creation data
     * @param user Current user
     * @return Created comment
     */
    public CommentDTO createComment(CommentCreateDTO createDTO, UserDTO user) {
        Comment comment = Comment.builder()
                .discussionId(createDTO.getDiscussionId())
                .content(createDTO.getContent())
                .userId(user.getId())
                .userName(user.getUsername())
                .parentId(createDTO.getParentId())
                .attachments(createDTO.getAttachments())
                .createTime(new Date())
                .build();
        
        Comment savedComment = commentRepository.save(comment);
        
        // Update comment count in discussion
        int commentCount = commentRepository.countByDiscussionIdAndIsDeletedFalse(createDTO.getDiscussionId());
        discussionService.updateCommentCount(createDTO.getDiscussionId(), commentCount);
        
        // Create notification if this is a reply
        if (createDTO.getParentId() != null) {
            Comment parentComment = commentRepository.findById(createDTO.getParentId()).orElse(null);
            if (parentComment != null && !parentComment.getUserId().equals(user.getId())) {
                notificationService.createCommentReplyNotification(
                        parentComment.getUserId(), 
                        createDTO.getDiscussionId(),
                        savedComment.getId(),
                        user.getUsername()
                );
            }
        }
        
        return convertToDTO(savedComment);
    }
    
    /**
     * Get comments for a discussion with pagination
     *
     * @param discussionId Discussion ID
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of comments with nested replies
     */
    public Page<CommentDTO> getCommentsByDiscussionId(String discussionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Page<Comment> commentPage = commentRepository.findByDiscussionIdAndIsDeletedFalseAndParentIdIsNullOrderByCreateTimeDesc(
                discussionId, pageable);
                
        return commentPage.map(comment -> {
            CommentDTO dto = convertToDTO(comment);
            
            // Add replies to this comment
            List<Comment> replies = commentRepository.findByParentIdAndIsDeletedFalseOrderByCreateTimeAsc(comment.getId());
            dto.setReplies(replies.stream().map(this::convertToDTO).collect(Collectors.toList()));
            
            return dto;
        });
    }
    
    /**
     * Like or unlike a comment
     *
     * @param commentId Comment ID
     * @param user Current user
     * @return Updated comment
     */
    public CommentDTO toggleLike(String commentId, UserDTO user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        List<String> likedByUsers = comment.getLikedByUsers();
        
        if (likedByUsers.contains(user.getId())) {
            // User already liked the comment, so unlike it
            likedByUsers.remove(user.getId());
        } else {
            // User hasn't liked the comment, so like it
            likedByUsers.add(user.getId());
            
            // Create notification for comment like (if not the user's own comment)
            if (!comment.getUserId().equals(user.getId())) {
                notificationService.createCommentLikeNotification(
                        comment.getUserId(), 
                        comment.getDiscussionId(), 
                        commentId, 
                        user.getUsername()
                );
            }
        }
        
        comment.setLikedByUsers(likedByUsers);
        Comment updatedComment = commentRepository.save(comment);
        
        return convertToDTO(updatedComment);
    }
    
    /**
     * Update comment
     *
     * @param commentId Comment ID
     * @param content New content
     * @param user Current user
     * @return Updated comment
     * @throws RuntimeException If comment not found or user not authorized
     */
    public CommentDTO updateComment(String commentId, String content, UserDTO user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        // Check if user is the owner
        if (!comment.getUserId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to update this comment");
        }
        
        comment.setContent(content);
        comment.setUpdateTime(new Date());
        
        Comment updatedComment = commentRepository.save(comment);
        return convertToDTO(updatedComment);
    }
    
    /**
     * Delete comment (soft delete)
     *
     * @param commentId Comment ID
     * @param user Current user
     * @throws RuntimeException If comment not found or user not authorized
     */
    public void deleteComment(String commentId, UserDTO user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        
        // Check if user is the owner
        if (!comment.getUserId().equals(user.getId()) && !"ADMIN".equals(user.getRole())) {
            throw new RuntimeException("Not authorized to delete this comment");
        }
        
        comment.setDeleted(true);
        commentRepository.save(comment);
        
        // Update comment count in discussion
        int commentCount = commentRepository.countByDiscussionIdAndIsDeletedFalse(comment.getDiscussionId());
        discussionService.updateCommentCount(comment.getDiscussionId(), commentCount);
    }
    
    /**
     * Get comments by user ID
     *
     * @param userId User ID
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of comments
     */
    public Page<CommentDTO> getCommentsByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return commentRepository.findByUserIdAndIsDeletedFalseOrderByCreateTimeDesc(userId, pageable)
                .map(this::convertToDTO);
    }
    
    /**
     * Convert Comment entity to DTO
     *
     * @param comment Comment entity
     * @return Comment DTO
     */
    private CommentDTO convertToDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .discussionId(comment.getDiscussionId())
                .content(comment.getContent())
                .userId(comment.getUserId())
                .userName(comment.getUserName())
                .parentId(comment.getParentId())
                .attachments(comment.getAttachments())
                .createTime(comment.getCreateTime())
                .updateTime(comment.getUpdateTime())
                .likedByUsers(comment.getLikedByUsers())
                .likesCount(comment.getLikedByUsers().size())
                .replies(new ArrayList<>())
                .build();
    }
}
