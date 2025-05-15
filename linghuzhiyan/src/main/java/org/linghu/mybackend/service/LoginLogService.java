package org.linghu.mybackend.service;

import org.linghu.mybackend.domain.LoginLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 登录日志服务接口
 */
public interface LoginLogService {
    
    /**
     * 记录登录成功的日志
     * @param userId 用户ID
     * @param ipAddress 登录IP地址
     * @param deviceType 设备类型
     * @param loginInfo 额外的登录信息（JSON格式）
     * @return 保存的登录日志记录
     */
    LoginLog logSuccessfulLogin(String userId, String ipAddress, String deviceType, String loginInfo);
    
    /**
     * 记录登录失败的日志
     * @param userId 用户ID
     * @param ipAddress 登录IP地址
     * @param deviceType 设备类型
     * @param failureReason 失败原因
     * @param loginInfo 额外的登录信息（JSON格式）
     * @return 保存的登录日志记录
     */
    LoginLog logFailedLogin(String userId, String ipAddress, String deviceType, String failureReason, String loginInfo);
    
    /**
     * 获取用户的登录历史
     * @param userId 用户ID
     * @return 用户的登录日志列表
     */
    List<LoginLog> getUserLoginHistory(String userId);
    
    /**
     * 分页获取用户的登录历史
     * @param userId 用户ID
     * @param pageable 分页信息
     * @return 分页的登录日志
     */
    Page<LoginLog> getUserLoginHistory(String userId, Pageable pageable);
    
    /**
     * 检查用户是否被锁定（短时间内多次登录失败）
     * @param userId 用户ID
     * @param lockThreshold 锁定阈值（失败次数）
     * @param minutes 检查的时间范围（分钟）
     * @return 是否应该被锁定
     */
    boolean isUserLocked(String userId, int lockThreshold, int minutes);
    
    /**
     * 获取可疑的登录活动
     * @param threshold 失败阈值
     * @param minutes 检查的时间范围（分钟）
     * @return IP地址和失败次数的映射
     */
    Map<String, Long> getSuspiciousLoginActivities(int threshold, int minutes);
    
    /**
     * 清理旧的登录日志
     * @param days 保留天数，超过这个天数的日志将被删除
     * @return 删除的记录数
     */
    long cleanupOldLogs(int days);
}
