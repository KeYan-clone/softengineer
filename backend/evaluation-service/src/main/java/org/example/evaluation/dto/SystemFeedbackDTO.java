package org.example.evaluation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统反馈的数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemFeedbackDTO {

    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @NotBlank(message = "反馈类别不能为空")
    private String category;

    @NotBlank(message = "反馈内容不能为空")
    @Size(max = 1000, message = "反馈内容长度不能超过1000字符")
    private String content;
    
    @Size(max = 500, message = "建议长度不能超过500字符")
    private String suggestion;
}
