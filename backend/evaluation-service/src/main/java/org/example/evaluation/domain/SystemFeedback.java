package org.example.evaluation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 系统反馈实体类
 * 用于收集用户对系统的反馈
 */
@Entity
@Table(name = "system_feedbacks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, length = 1000)
    private String content;
    
    @Column(length = 500)
    private String suggestion;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedbackStatus status;
    
    @Column(length = 500)
    private String adminResponse;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @Version
    private Integer version;
    
    /**
     * 反馈状态枚举
     */
    public enum FeedbackStatus {
        SUBMITTED,   // 已提交
        PROCESSING,  // 处理中
        RESOLVED,    // 已解决
        CLOSED       // 已关闭
    }
}
