-- 初始化评价服务数据库

-- 创建实验评价表
CREATE TABLE IF NOT EXISTS experiment_evaluation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    experiment_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_experiment_id (experiment_id),
    INDEX idx_user_id (user_id),
    UNIQUE KEY uk_experiment_user (experiment_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建评价标准评分表
CREATE TABLE IF NOT EXISTS criteria_rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    evaluation_id BIGINT NOT NULL,
    criteria_name VARCHAR(100) NOT NULL,
    rating INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (evaluation_id) REFERENCES experiment_evaluation(id) ON DELETE CASCADE,
    INDEX idx_evaluation_id (evaluation_id),
    UNIQUE KEY uk_evaluation_criteria (evaluation_id, criteria_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建系统反馈表
CREATE TABLE IF NOT EXISTS system_feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    suggestion TEXT,
    status ENUM('SUBMITTED', 'PROCESSING', 'RESOLVED', 'REJECTED') NOT NULL DEFAULT 'SUBMITTED',
    admin_response TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_category (category),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 添加一些示例数据（开发环境使用）
INSERT INTO experiment_evaluation (experiment_id, user_id, rating, comment) VALUES
('exp-001', 'user-001', 5, '这个实验非常棒，操作简单，结果准确。'),
('exp-001', 'user-002', 4, '实验设计很好，但界面可以更友好。'),
('exp-002', 'user-001', 3, '实验结果比较准确，但过程有点复杂。'),
('exp-002', 'user-003', 5, '非常喜欢这个实验的创新设计。'),
('exp-003', 'user-002', 2, '实验结果不太准确，需要改进。');

INSERT INTO criteria_rating (evaluation_id, criteria_name, rating) VALUES
(1, 'accuracy', 5),
(1, 'usability', 4),
(1, 'innovation', 5),
(2, 'accuracy', 5),
(2, 'usability', 3),
(2, 'innovation', 4),
(3, 'accuracy', 4),
(3, 'usability', 2),
(3, 'innovation', 3),
(4, 'accuracy', 5),
(4, 'usability', 5),
(4, 'innovation', 5),
(5, 'accuracy', 2),
(5, 'usability', 3),
(5, 'innovation', 1);

INSERT INTO system_feedback (user_id, category, content, suggestion, status) VALUES
('user-001', 'interface', '主界面在移动端显示不正确', '建议优化移动端适配', 'SUBMITTED'),
('user-002', 'performance', '实验加载时间过长', '建议优化后端处理速度', 'PROCESSING'),
('user-003', 'feature', '希望能添加实验对比功能', '可以增加多实验结果并排展示', 'RESOLVED'),
('user-001', 'bug', '提交评价时偶尔出现404错误', '请检查服务器连接问题', 'SUBMITTED'),
('user-004', 'other', '希望能提供更多学习资源', '可以在每个实验后增加相关学习材料', 'REJECTED');
