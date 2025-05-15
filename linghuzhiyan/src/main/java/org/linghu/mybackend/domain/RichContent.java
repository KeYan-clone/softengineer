package org.linghu.mybackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RichContent {
    private String html; // HTML格式内容
    private Object delta; // Draft.js Delta格式内容
}
