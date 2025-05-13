package org.example.discussion.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Comment entity represents a comment made on a discussion
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;
    
    private String discussionId;
    
    private String content;
    
    private String userId;
    
    private String userName;
    
    // For nested comments/replies
    private String parentId;
    
    @Builder.Default
    private List<String> attachments = new ArrayList<>();
    
    @Builder.Default
    private Date createTime = new Date();
    
    private Date updateTime;
    
    @Builder.Default
    private boolean isDeleted = false;
    
    // For comment likes
    @Builder.Default
    private List<String> likedByUsers = new ArrayList<>();
}
