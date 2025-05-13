package org.example.discussion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO for returning discussion information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionDTO {
    
    private String id;
    private String title;
    private String content;
    private String userId;
    private String userName;
    private List<String> tags = new ArrayList<>();
    private List<String> attachments = new ArrayList<>();
    private Integer viewCount;
    private Integer commentCount;
    private Date createTime;
    private Date updateTime;
    private String experimentId;
}
