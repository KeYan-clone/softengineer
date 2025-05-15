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
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;
    
    private String discussionId;
    private String content;
    private RichContent richContent;
    
    private String userId;
    private String username;
    private String userAvatar;
    
    private String parentId;
    private String rootId;   // 用于评论树
    private String path;     // 评论路径(用于排序和查询)
    private Integer depth;   // 评论深度
    
    private String replyToUserId;
    private String replyToUsername;
    
    @Builder.Default
    private Integer likeCount = 0;
    @Builder.Default
    private List<String> likedBy = new ArrayList<>();
    
    @Builder.Default
    private List<Attachment> attachments = new ArrayList<>();
    
    @Builder.Default
    private String status = "VISIBLE"; // VISIBLE, HIDDEN, FLAGGED
    
    @Builder.Default
    private Boolean deleted = false;
    
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
