package org.example.discussion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO for returning comment information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    
    private String id;
    private String discussionId;
    private String content;
    private String userId;
    private String userName;
    private String parentId;
    private List<String> attachments = new ArrayList<>();
    private Date createTime;
    private Date updateTime;
    private List<String> likedByUsers = new ArrayList<>();
    private Integer likesCount;
    private List<CommentDTO> replies = new ArrayList<>();
}
