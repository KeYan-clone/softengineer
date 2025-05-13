package org.example.discussion.controller;

import lombok.RequiredArgsConstructor;
import org.example.discussion.dto.NotificationDTO;
import org.example.discussion.service.NotificationService;
import org.example.discussion.util.ApiResponse;
import org.example.discussion.util.AuthUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for notification-related operations
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Get notifications for the current user
     *
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of notifications
     */
    @GetMapping
    public ResponseEntity<ApiResponse.Result<ApiResponse.PageResult<NotificationDTO>>> getUserNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<NotificationDTO> notifications = notificationService.getUserNotifications(AuthUtil.getCurrentUser(), page, size);
        return ResponseEntity.ok(ApiResponse.success(ApiResponse.page(notifications)));
    }

    /**
     * Mark notification as read
     *
     * @param notificationId Notification ID
     * @return Updated notification
     */
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse.Result<NotificationDTO>> markAsRead(@PathVariable String notificationId) {
        NotificationDTO notification = notificationService.markAsRead(notificationId, AuthUtil.getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success(notification));
    }

    /**
     * Mark all notifications as read for current user
     *
     * @return Success message
     */
    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse.Result<Void>> markAllAsRead() {
        notificationService.markAllAsRead(AuthUtil.getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read"));
    }

    /**
     * Delete notification
     *
     * @param notificationId Notification ID
     * @return Success message
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<ApiResponse.Result<Void>> deleteNotification(@PathVariable String notificationId) {
        notificationService.deleteNotification(notificationId, AuthUtil.getCurrentUser());
        return ResponseEntity.ok(ApiResponse.success("Notification deleted successfully"));
    }

    /**
     * Count unread notifications for current user
     *
     * @return Number of unread notifications
     */
    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse.Result<Integer>> countUnreadNotifications() {
        int count = notificationService.countUnreadNotifications(AuthUtil.getCurrentUser().getId());
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}
