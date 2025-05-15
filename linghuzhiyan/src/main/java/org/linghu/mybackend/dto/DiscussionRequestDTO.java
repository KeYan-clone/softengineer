package org.linghu.mybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.linghu.mybackend.domain.RichContent;
import org.linghu.mybackend.domain.Attachment;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionRequestDTO {
    private String title;
    private String content;
    private RichContent richContent;
    private List<String> tags;
    private String experimentId;
    private List<AttachmentDTO> attachments;
}
