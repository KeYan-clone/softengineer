package org.example.discussion.controller;

import lombok.RequiredArgsConstructor;
import org.example.discussion.dto.MessageCreateDTO;
import org.example.discussion.dto.MessageDTO;
import org.example.discussion.service.MessageService;
import org.example.discussion.util.ApiResponse;
import org.example.discussion.util.AuthUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controller for message-related operations
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * Send a private message
     *
     * @param createDTO Message creation data
     * @return Created message
     */
    @PostMapping
    public ResponseEntity<ApiResponse.Result<MessageDTO>> sendMessage(@Valid @RequestBody MessageCreateDTO createDTO) {
        MessageDTO message = messageService.sendMessage(createDTO, AuthUtil.getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    /**
     * Get messages for the current user
     *
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of messages
     */
    @GetMapping
    public ResponseEntity<ApiResponse.Result<ApiResponse.PageResult<MessageDTO>>> getUserMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<MessageDTO> messages = messageService.getUserMessages(AuthUtil.getCurrentUser(), page, size);
        return ResponseEntity.ok(ApiResponse.success(ApiResponse.page(messages)));
    }

    /**
     * Get conversation between current user and another user
     *
     * @param userId Other user ID
     * @param userName Other user name
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of messages
     */
    @GetMapping("/conversation/{userId}")
    public ResponseEntity<ApiResponse.Result<ApiResponse.PageResult<MessageDTO>>> getConversation(
            @PathVariable String userId,
            @RequestParam(required = false) String userName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<MessageDTO> messages = messageService.getConversation(AuthUtil.getCurrentUser(), userId, userName, page, size);
        return ResponseEntity.ok(ApiResponse.success(ApiResponse.page(messages)));
    }

    /**
     * Mark message as read
     *
     * @param messageId Message ID
     * @return Updated message
     */
    @PutMapping("/{messageId}/read")
    public ResponseEntity<ApiResponse.Result<MessageDTO>> markAsRead(@PathVariable String messageId) {
        MessageDTO message = messageService.markAsRead(messageId, AuthUtil.getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success(message));
    }

    /**
     * Delete message
     *
     * @param messageId Message ID
     * @return Success message
     */
    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse.Result<Void>> deleteMessage(@PathVariable String messageId) {
        messageService.deleteMessage(messageId, AuthUtil.getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success("Message deleted successfully"));
    }

    /**
     * Count unread messages for current user
     *
     * @return Number of unread messages
     */
    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse.Result<Integer>> countUnreadMessages() {
        int count = messageService.countUnreadMessages(AuthUtil.getCurrentUser().getId());
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}
