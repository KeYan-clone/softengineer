package org.linghu.mybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.linghu.mybackend.dto.DiscussionRequestDTO;
import org.linghu.mybackend.dto.DiscussionResponseDTO;
import org.linghu.mybackend.dto.PriorityRequestDTO;
import org.linghu.mybackend.dto.ReviewRequestDTO;
import org.linghu.mybackend.service.DiscussionService;
import org.linghu.mybackend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/discussions")
@RequiredArgsConstructor
@Tag(name = "Discussion Controller", description = "讨论模块相关接口")
public class DiscussionController {

    private final DiscussionService discussionService;
    private final UserService userService;

    @PostMapping
    @Operation(summary = "创建讨论", description = "创建一个新的讨论主题")
    public ResponseEntity<DiscussionResponseDTO> createDiscussion(@RequestBody DiscussionRequestDTO requestDTO) {
        String userId = userService.getCurrentUserId();
        DiscussionResponseDTO responseDTO = discussionService.createDiscussion(requestDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    @Operation(summary = "获取讨论列表", description = "分页获取讨论列表，支持多种过滤和排序")
    public ResponseEntity<Page<DiscussionResponseDTO>> getDiscussions(
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String experimentId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "lastActivityTime") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String order,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        String currentUserId = null;
        try {
            currentUserId = userService.getCurrentUserId();
        } catch (Exception e) {
            // 未登录用户不影响浏览讨论
        }

        String[] tagArray = tags != null ? tags.split(",") : null;

        Page<DiscussionResponseDTO> discussions = discussionService.getDiscussions(
                tagArray, experimentId, userId, status, keyword, sortBy, order, page, size, currentUserId);

        return ResponseEntity.ok(discussions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取讨论详情", description = "根据ID获取讨论详情")
    public ResponseEntity<DiscussionResponseDTO> getDiscussionById(@PathVariable String id) {
        String currentUserId = null;
        try {
            currentUserId = userService.getCurrentUserId();
        } catch (Exception e) {
            // 未登录用户不影响浏览讨论
        }

        DiscussionResponseDTO discussion = discussionService.getDiscussionById(id, currentUserId);
        return ResponseEntity.ok(discussion);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新讨论", description = "更新讨论内容")
    public ResponseEntity<DiscussionResponseDTO> updateDiscussion(
            @PathVariable String id,
            @RequestBody DiscussionRequestDTO requestDTO) {

        String userId = userService.getCurrentUserId();
        DiscussionResponseDTO updatedDiscussion = discussionService.updateDiscussion(id, requestDTO, userId);
        return ResponseEntity.ok(updatedDiscussion);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除讨论", description = "删除指定ID的讨论")
    public ResponseEntity<Map<String, Boolean>> deleteDiscussion(@PathVariable String id) {
        String userId = userService.getCurrentUserId();
        boolean deleted = discussionService.deleteDiscussion(id, userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", deleted);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/review")
    @Operation(summary = "审核讨论", description = "审核讨论内容")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_TEACHER','ROLE_ASSISTANT')")
    public ResponseEntity<DiscussionResponseDTO> reviewDiscussion(
            @PathVariable String id,
            @RequestBody ReviewRequestDTO requestDTO) {

        String reviewerId = userService.getCurrentUserId();
        DiscussionResponseDTO reviewedDiscussion = discussionService.reviewDiscussion(id, requestDTO, reviewerId);
        return ResponseEntity.ok(reviewedDiscussion);
    }

    @PutMapping("/{id}/priority")
    @Operation(summary = "设置讨论优先级", description = "设置讨论的优先级(置顶)")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_TEACHER','ROLE_ASSISTANT')")

    public ResponseEntity<DiscussionResponseDTO> updatePriority(
            @PathVariable String id,
            @RequestBody PriorityRequestDTO requestDTO) {

        String userId = userService.getCurrentUserId();
        DiscussionResponseDTO updatedDiscussion = discussionService.updatePriority(id, requestDTO, userId);
        return ResponseEntity.ok(updatedDiscussion);
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "点赞/取消点赞讨论", description = "对讨论进行点赞或取消点赞")
    public ResponseEntity<DiscussionResponseDTO> toggleLike(@PathVariable String id) {
        String userId = userService.getCurrentUserId();
        DiscussionResponseDTO discussion = discussionService.toggleLike(id, userId);
        return ResponseEntity.ok(discussion);
    }
}
