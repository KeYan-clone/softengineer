package org.linghu.mybackend.service;

import org.linghu.mybackend.domain.Comment;
import org.linghu.mybackend.dto.CommentRequestDTO;
import org.linghu.mybackend.dto.CommentResponseDTO;
import org.linghu.mybackend.dto.ReportCommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    
    CommentResponseDTO createComment(String discussionId, CommentRequestDTO requestDTO, String userId);
    
    Page<CommentResponseDTO> getCommentsByDiscussionId(
            String discussionId, 
            boolean rootOnly, 
            String sortBy, 
            String order, 
            int page, 
            int size,
            String currentUserId);
    
    Page<CommentResponseDTO> getRepliesByCommentId(
            String commentId, 
            int page, 
            int size,
            String currentUserId);
    
    void deleteComment(String commentId, String userId);
    
    CommentResponseDTO toggleLike(String commentId, String userId);
    
    CommentResponseDTO getCommentById(String commentId, String currentUserId);
    
    void reportComment(String commentId, ReportCommentDTO reportDTO, String userId);
}
