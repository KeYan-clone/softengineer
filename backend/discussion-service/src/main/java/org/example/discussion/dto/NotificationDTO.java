package org.example.discussion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.discussion.domain.Notification.NotificationType;

import java.util.Date;

/**
 * DTO for returning notification information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    
    private String id;
    private String userId;
    private String title;
    private String content;
    private NotificationType type;
    private String referenceId;
    private Date createTime;
    private boolean isRead;
}
