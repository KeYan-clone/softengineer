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
 * Discussion entity represents a discussion topic created by users
 */
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
    
    private String userId;
    
    private String userName;
    
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    
    @Builder.Default
    private List<String> attachments = new ArrayList<>();
    
    @Builder.Default
    private Integer viewCount = 0;
    
    @Builder.Default
    private Integer commentCount = 0;
    
    @Builder.Default
    private Date createTime = new Date();
    
    private Date updateTime;
    
    // Optional: If the discussion is related to an experiment
    private String experimentId;
    
    @Builder.Default
    private boolean isDeleted = false;
}
