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
 * Message entity represents a private message between users
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class Message {
    @Id
    private String id;
    
    private String senderId;
    
    private String receiverId;
    
    private String content;
    
    @Builder.Default
    private List<String> attachments = new ArrayList<>();
    
    @Builder.Default
    private Date createTime = new Date();
    
    @Builder.Default
    private boolean isRead = false;
    
    @Builder.Default
    private boolean isDeleted = false;
}
