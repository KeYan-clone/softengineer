package org.example.discussion.controller;

import lombok.RequiredArgsConstructor;
import org.example.discussion.dto.CommentCreateDTO;
import org.example.discussion.dto.CommentDTO;
import org.example.discussion.service.CommentService;
import org.example.discussion.util.ApiResponse;
import org.example.discussion.util.AuthUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controller for comment-related operations
 */
@RestController
@RequestMapping("/api/discussions")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * Create a new comment
     *
     * @param createDTO Comment creation data
     * @return Created comment
     */
    @PostMapping("/{discussionId}/comments")
    public ResponseEntity<ApiResponse.Result<CommentDTO>> createComment(
            @PathVariable String discussionId,
            @Valid @RequestBody CommentCreateDTO createDTO) {
        // Ensure discussionId in path and body match
        if (!discussionId.equals(createDTO.getDiscussionId())) {
            throw new IllegalArgumentException("Discussion ID in path and body must match");
        }
        CommentDTO comment = commentService.createComment(createDTO, AuthUtil.getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success(comment));
    }

    /**
     * Get comments for a discussion with pagination
     *
     * @param discussionId Discussion ID
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of comments with nested replies
     */
    @GetMapping("/{discussionId}/comments")
    public ResponseEntity<ApiResponse.Result<ApiResponse.PageResult<CommentDTO>>> getCommentsByDiscussionId(
            @PathVariable String discussionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CommentDTO> comments = commentService.getCommentsByDiscussionId(discussionId, page, size);
        return ResponseEntity.ok(ApiResponse.success(ApiResponse.page(comments)));
    }

    /**
     * Like or unlike a comment
     *
     * @param commentId Comment ID
     * @return Updated comment
     */
    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<ApiResponse.Result<CommentDTO>> toggleLike(@PathVariable String commentId) {
        CommentDTO comment = commentService.toggleLike(commentId, AuthUtil.getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success(comment));
    }

    /**
     * Update comment
     *
     * @param commentId Comment ID
     * @param content New content
     * @return Updated comment
     */
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse.Result<CommentDTO>> updateComment(
            @PathVariable String commentId,
            @RequestBody String content) {
        CommentDTO comment = commentService.updateComment(commentId, content, AuthUtil.getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success(comment));
    }

    /**
     * Delete comment
     *
     * @param commentId Comment ID
     * @return Success message
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse.Result<Void>> deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(commentId, AuthUtil.getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success("Comment deleted successfully"));
    }

    /**
     * Get comments by user ID
     *
     * @param userId User ID
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of comments
     */
    @GetMapping("/users/{userId}/comments")
    public ResponseEntity<ApiResponse.Result<ApiResponse.PageResult<CommentDTO>>> getCommentsByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CommentDTO> comments = commentService.getCommentsByUserId(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(ApiResponse.page(comments)));
    }
}
