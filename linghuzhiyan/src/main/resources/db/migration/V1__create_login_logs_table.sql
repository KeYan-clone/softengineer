-- 创建登录日志表
CREATE TABLE IF NOT EXISTS login_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    ip_address VARCHAR(45) NOT NULL,
    device_type VARCHAR(30),
    status VARCHAR(20) NOT NULL,
    failure_reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    login_info JSON,
    
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at),
    INDEX idx_status (status)
);

-- 注释说明
COMMENT ON TABLE login_logs IS '用户登录日志表';
COMMENT ON COLUMN login_logs.id IS '主键ID';
COMMENT ON COLUMN login_logs.user_id IS '用户ID';
COMMENT ON COLUMN login_logs.ip_address IS '登录IP地址';
COMMENT ON COLUMN login_logs.device_type IS '设备类型';
COMMENT ON COLUMN login_logs.status IS '登录状态：SUCCESS成功，FAILED失败';
COMMENT ON COLUMN login_logs.failure_reason IS '失败原因';
COMMENT ON COLUMN login_logs.created_at IS '创建时间';
COMMENT ON COLUMN login_logs.login_info IS '登录的其他信息，JSON格式';
