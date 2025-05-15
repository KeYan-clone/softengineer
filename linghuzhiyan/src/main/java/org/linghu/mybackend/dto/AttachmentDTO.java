package org.linghu.mybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDTO {
    private String url;
    private String type;     // image, video, document, etc.
    private String name;
    private Long size;       // bytes
    private String thumbnailUrl;
}
