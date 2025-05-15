package org.linghu.mybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.linghu.mybackend.domain.RichContent;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {
    private String content;
    private RichContentDTO richContent;
    private String parentId;
    private String replyToUserId;
    private List<AttachmentDTO> attachments;
}
