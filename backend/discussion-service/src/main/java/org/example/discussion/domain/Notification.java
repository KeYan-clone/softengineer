package org.example.discussion.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Notification entity represents a system notification to a user
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
    
    private String userId;
    
    private String title;
    
    private String content;
    
    private NotificationType type;
    
    // Reference ID to the related entity (e.g., discussionId, commentId)
    private String referenceId;
    
    @Builder.Default
    private Date createTime = new Date();
    
    @Builder.Default
    private boolean isRead = false;
    
    @Builder.Default
    private boolean isDeleted = false;
    
    /**
     * Types of notifications in the system
     */
    public enum NotificationType {
        NEW_COMMENT,
        NEW_REPLY,
        MENTION,
        LIKE,
        SYSTEM
    }
}
