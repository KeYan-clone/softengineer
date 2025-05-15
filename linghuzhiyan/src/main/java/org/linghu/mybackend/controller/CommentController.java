package org.linghu.mybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.linghu.mybackend.dto.CommentRequestDTO;
import org.linghu.mybackend.dto.CommentResponseDTO;
import org.linghu.mybackend.dto.ReportCommentDTO;
import org.linghu.mybackend.service.CommentService;
import org.linghu.mybackend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Comment Controller", description = "评论模块相关接口")
public class CommentController {
    
    private final CommentService commentService;
    private final UserService userService;
    
    @PostMapping("/api/discussions/{discussionId}/comments")
    @Operation(summary = "创建评论", description = "为指定的讨论创建新评论")
    public ResponseEntity<CommentResponseDTO> createComment(
            @PathVariable String discussionId,
            @RequestBody CommentRequestDTO requestDTO) {
        
        String userId = userService.getCurrentUserId();
        CommentResponseDTO responseDTO = commentService.createComment(discussionId, requestDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
    
    @GetMapping("/api/discussions/{discussionId}/comments")
    @Operation(summary = "获取讨论的评论列表", description = "分页获取指定讨论的评论列表")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByDiscussionId(
            @PathVariable String discussionId,
            @RequestParam(required = false, defaultValue = "false") boolean rootOnly,
            @RequestParam(required = false, defaultValue = "createTime") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        
        String currentUserId = null;
        try {
            currentUserId = userService.getCurrentUserId();
        } catch (Exception e) {
            // 未登录用户不影响浏览评论
        }
        
        Page<CommentResponseDTO> comments = commentService.getCommentsByDiscussionId(
                discussionId, rootOnly, sortBy, order, page, size, currentUserId);
        
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/api/comments/{commentId}/replies")
    @Operation(summary = "获取评论的回复", description = "分页获取指定评论的回复列表")
    public ResponseEntity<Page<CommentResponseDTO>> getRepliesByCommentId(
            @PathVariable String commentId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        
        String currentUserId = null;
        try {
            currentUserId = userService.getCurrentUserId();
        } catch (Exception e) {
            // 未登录用户不影响浏览评论
        }
        
        Page<CommentResponseDTO> replies = commentService.getRepliesByCommentId(commentId, page, size, currentUserId);
        return ResponseEntity.ok(replies);
    }
    
    @DeleteMapping("/api/comments/{commentId}")
    @Operation(summary = "删除评论", description = "删除指定ID的评论")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId) {
        String userId = userService.getCurrentUserId();
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/api/comments/{commentId}/like")
    @Operation(summary = "点赞/取消点赞评论", description = "对评论进行点赞或取消点赞")
    public ResponseEntity<CommentResponseDTO> toggleLike(@PathVariable String commentId) {
        String userId = userService.getCurrentUserId();
        CommentResponseDTO comment = commentService.toggleLike(commentId, userId);
        return ResponseEntity.ok(comment);
    }
    
    @GetMapping("/api/comments/{commentId}")
    @Operation(summary = "获取评论详情", description = "根据ID获取评论详情")
    public ResponseEntity<CommentResponseDTO> getCommentById(@PathVariable String commentId) {
        String currentUserId = null;
        try {
            currentUserId = userService.getCurrentUserId();
        } catch (Exception e) {
            // 未登录用户不影响浏览评论
        }
        
        CommentResponseDTO comment = commentService.getCommentById(commentId, currentUserId);
        return ResponseEntity.ok(comment);
    }
    
    @PostMapping("/api/comments/{commentId}/report")
    @Operation(summary = "举报评论", description = "举报不当评论内容")
    public ResponseEntity<Void> reportComment(
            @PathVariable String commentId,
            @RequestBody ReportCommentDTO reportDTO) {
        
        String userId = userService.getCurrentUserId();
        commentService.reportComment(commentId, reportDTO, userId);
        return ResponseEntity.noContent().build();
    }
}
