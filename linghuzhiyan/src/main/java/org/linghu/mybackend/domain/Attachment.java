package org.linghu.mybackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    private String url;      // 附件URL
    private String type;     // 类型: image, video, document, etc.
    private String name;     // 文件名
    private Long size;       // 文件大小(bytes)
    private String thumbnailUrl; // 缩略图URL，主要用于评论中的附件
}
