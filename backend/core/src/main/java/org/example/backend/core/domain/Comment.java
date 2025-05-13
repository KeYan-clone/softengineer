package org.example.backend.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * Comment domain model
 * Core domain entity that represents a comment in a discussion
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    
    /**
     * Comment unique identifier
     */
    @Id
    private String id;
    
    /**
     * Comment content
     */
    private String content;
    
    /**
     * ID of the user who created the comment
     */
    private String userId;
    
    /**
     * Username of the user who created the comment
     */
    private String username;
    
    /**
     * ID of the user being replied to (optional)
     */
    private String replyToId;
    
    /**
     * Username of the user being replied to (optional)
     */
    private String replyToUsername;
    
    /**
     * Creation timestamp
     */
    private LocalDateTime createTime;
    
    /**
     * Deleted flag (soft delete)
     */
    @Builder.Default
    private boolean deleted = false;
}
