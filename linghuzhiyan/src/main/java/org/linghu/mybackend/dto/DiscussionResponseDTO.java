package org.linghu.mybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionResponseDTO {
    private String id;
    private String title;
    private String content;
    private RichContentDTO richContent;
    private String userId;
    private String username;
    private String userAvatar;
    private java.util.List<String> tags;
    private String experimentId;
    private String status;
    private String rejectionReason;
    private Integer priority;
    private Long viewCount;
    private Long commentCount;
    private Long likeCount;
    private Boolean isLiked;
    private java.time.LocalDateTime lastCommentTime;
    private java.time.LocalDateTime lastActivityTime;
    private java.util.List<AttachmentDTO> attachments;
    private java.time.LocalDateTime createTime;
    private java.time.LocalDateTime updateTime;
    private java.time.LocalDateTime approvedTime;
}
