package org.linghu.mybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private String id;
    private String discussionId;
    private String content;
    private RichContentDTO richContent;
    private String userId;
    private String username;
    private String userAvatar;
    private String parentId;
    private String rootId;
    private String path;
    private Integer depth;
    private String replyToUserId;
    private String replyToUsername;
    private Integer likeCount;
    private Boolean isLiked;
    private List<AttachmentDTO> attachments;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    @Builder.Default
    private List<CommentResponseDTO> replies = new ArrayList<>();
}
