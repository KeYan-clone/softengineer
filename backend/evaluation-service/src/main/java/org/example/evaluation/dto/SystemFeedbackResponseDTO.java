package org.example.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.evaluation.domain.SystemFeedback.FeedbackStatus;

import java.time.LocalDateTime;

/**
 * 系统反馈响应的数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemFeedbackResponseDTO {

    private Long id;
    private String userId;
    private String category;
    private String content;
    private String suggestion;
    private FeedbackStatus status;
    private String adminResponse;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
