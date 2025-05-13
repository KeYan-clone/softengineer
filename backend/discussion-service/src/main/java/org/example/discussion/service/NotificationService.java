package org.example.discussion.service;

import lombok.RequiredArgsConstructor;
import org.example.discussion.domain.Notification;
import org.example.discussion.domain.Notification.NotificationType;
import org.example.discussion.dto.NotificationDTO;
import org.example.discussion.dto.UserDTO;
import org.example.discussion.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Service for notification-related operations
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    
    /**
     * Create a notification for a new comment
     *
     * @param userId Target user ID
     * @param discussionId Discussion ID
     * @param commentId Comment ID
     * @param commenterName Name of the user who commented
     */
    public void createCommentNotification(String userId, String discussionId, String commentId, String commenterName) {
        Notification notification = Notification.builder()
                .userId(userId)
                .title("New Comment")
                .content(commenterName + " commented on your discussion")
                .type(NotificationType.NEW_COMMENT)
                .referenceId(discussionId)
                .createTime(new Date())
                .isRead(false)
                .build();
        
        Notification savedNotification = notificationRepository.save(notification);
        NotificationDTO dto = convertToDTO(savedNotification);
        
        // Send notification via WebSocket
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", dto);
    }
    
    /**
     * Create a notification for a comment reply
     *
     * @param userId Target user ID
     * @param discussionId Discussion ID
     * @param commentId Comment ID
     * @param replierName Name of the user who replied
     */
    public void createCommentReplyNotification(String userId, String discussionId, String commentId, String replierName) {
        Notification notification = Notification.builder()
                .userId(userId)
                .title("New Reply")
                .content(replierName + " replied to your comment")
                .type(NotificationType.NEW_REPLY)
                .referenceId(commentId)
                .createTime(new Date())
                .isRead(false)
                .build();
        
        Notification savedNotification = notificationRepository.save(notification);
        NotificationDTO dto = convertToDTO(savedNotification);
        
        // Send notification via WebSocket
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", dto);
    }
    
    /**
     * Create a notification for a comment like
     *
     * @param userId Target user ID
     * @param discussionId Discussion ID
     * @param commentId Comment ID
     * @param likerName Name of the user who liked
     */
    public void createCommentLikeNotification(String userId, String discussionId, String commentId, String likerName) {
        Notification notification = Notification.builder()
                .userId(userId)
                .title("New Like")
                .content(likerName + " liked your comment")
                .type(NotificationType.LIKE)
                .referenceId(commentId)
                .createTime(new Date())
                .isRead(false)
                .build();
        
        Notification savedNotification = notificationRepository.save(notification);
        NotificationDTO dto = convertToDTO(savedNotification);
        
        // Send notification via WebSocket
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", dto);
    }
    
    /**
     * Create a notification for a user mention
     *
     * @param userId Target user ID
     * @param discussionId Discussion ID
     * @param commentId Comment ID (optional)
     * @param mentionerName Name of the user who mentioned
     */
    public void createMentionNotification(String userId, String discussionId, String commentId, String mentionerName) {
        Notification notification = Notification.builder()
                .userId(userId)
                .title("New Mention")
                .content(mentionerName + " mentioned you")
                .type(NotificationType.MENTION)
                .referenceId(commentId != null ? commentId : discussionId)
                .createTime(new Date())
                .isRead(false)
                .build();
        
        Notification savedNotification = notificationRepository.save(notification);
        NotificationDTO dto = convertToDTO(savedNotification);
        
        // Send notification via WebSocket
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", dto);
    }
    
    /**
     * Create a system notification
     *
     * @param userId Target user ID
     * @param title Notification title
     * @param content Notification content
     * @param referenceId Reference ID (optional)
     */
    public void createSystemNotification(String userId, String title, String content, String referenceId) {
        Notification notification = Notification.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .type(NotificationType.SYSTEM)
                .referenceId(referenceId)
                .createTime(new Date())
                .isRead(false)
                .build();
        
        Notification savedNotification = notificationRepository.save(notification);
        NotificationDTO dto = convertToDTO(savedNotification);
        
        // Send notification via WebSocket
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", dto);
    }
    
    /**
     * Get user notifications with pagination
     *
     * @param user Current user
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of notifications
     */
    public Page<NotificationDTO> getUserNotifications(UserDTO user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return notificationRepository.findByUserIdAndIsDeletedFalseOrderByCreateTimeDesc(user.getId(), pageable)
                .map(this::convertToDTO);
    }
    
    /**
     * Mark notification as read
     *
     * @param notificationId Notification ID
     * @param user Current user
     * @return Updated notification
     * @throws RuntimeException If notification not found or user not authorized
     */
    public NotificationDTO markAsRead(String notificationId, UserDTO user) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        // Check if user is the owner
        if (!notification.getUserId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to update this notification");
        }
        
        notification.setRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        
        return convertToDTO(updatedNotification);
    }
    
    /**
     * Mark all notifications as read for a user
     *
     * @param user Current user
     */
    public void markAllAsRead(UserDTO user) {
        Page<Notification> notifications = notificationRepository.findByUserIdAndIsDeletedFalseOrderByCreateTimeDesc(
                user.getId(), PageRequest.of(0, 100));
                
        notifications.forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
    
    /**
     * Delete notification (soft delete)
     *
     * @param notificationId Notification ID
     * @param user Current user
     * @throws RuntimeException If notification not found or user not authorized
     */
    public void deleteNotification(String notificationId, UserDTO user) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        // Check if user is the owner
        if (!notification.getUserId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to delete this notification");
        }
        
        notification.setDeleted(true);
        notificationRepository.save(notification);
    }
    
    /**
     * Count unread notifications for a user
     *
     * @param userId User ID
     * @return Number of unread notifications
     */
    public int countUnreadNotifications(String userId) {
        return notificationRepository.countByUserIdAndIsReadFalseAndIsDeletedFalse(userId);
    }
    
    /**
     * Convert Notification entity to DTO
     *
     * @param notification Notification entity
     * @return Notification DTO
     */
    private NotificationDTO convertToDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .type(notification.getType())
                .referenceId(notification.getReferenceId())
                .createTime(notification.getCreateTime())
                .isRead(notification.isRead())
                .build();
    }
}
