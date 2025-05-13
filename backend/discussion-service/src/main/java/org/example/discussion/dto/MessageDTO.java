package org.example.discussion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DTO for returning message information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    
    private String id;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private String content;
    private List<String> attachments = new ArrayList<>();
    private Date createTime;
    private boolean isRead;
}
