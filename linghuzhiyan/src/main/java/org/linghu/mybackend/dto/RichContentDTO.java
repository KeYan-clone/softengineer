package org.linghu.mybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.linghu.mybackend.domain.RichContent;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RichContentDTO {
    private String html;
    private Object delta;
}
