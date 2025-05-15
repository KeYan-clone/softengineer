package org.linghu.mybackend.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "discussions")
public class Discussion {
    @Id
    private String id;
    
    private String title;
    private String content;
    private RichContent richContent;
    
    private String userId;
    private String username;
    private String userAvatar;
    
    private List<String> tags;
    private String experimentId;
    
    @Builder.Default
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED
    private String rejectionReason;
    
    @Builder.Default
    private Integer priority = 0;
    
    @Builder.Default
    private Long viewCount = 0L;
    @Builder.Default
    private Long commentCount = 0L;
    @Builder.Default
    private Long likeCount = 0L;
    @Builder.Default
    private List<String> likedBy = new ArrayList<>();
    
    private LocalDateTime lastCommentTime;
    private LocalDateTime lastActivityTime;
    
    @Builder.Default
    private List<Attachment> attachments = new ArrayList<>();
    
    @Builder.Default
    private Boolean deleted = false;
    
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime approvedTime;
    private String approvedBy;
}
