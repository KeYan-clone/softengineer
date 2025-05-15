package org.linghu.mybackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.linghu.mybackend.domain.LoginLog;
import org.linghu.mybackend.repository.LoginLogRepository;
import org.linghu.mybackend.service.LoginLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录日志服务实现类
 */
@Service
@RequiredArgsConstructor
public class LoginLogServiceImpl implements LoginLogService {

    private final LoginLogRepository loginLogRepository;

    /**
     * 记录登录成功的日志
     */
    @Override
    @Transactional
    public LoginLog logSuccessfulLogin(String userId, String ipAddress, String deviceType, String loginInfo) {
        LoginLog loginLog = LoginLog.builder()
                .userId(userId)
                .ipAddress(ipAddress)
                .deviceType(deviceType)
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .loginInfo(loginInfo)
                .build();
        
        return loginLogRepository.save(loginLog);
    }

    /**
     * 记录登录失败的日志
     */
    @Override
    @Transactional
    public LoginLog logFailedLogin(String userId, String ipAddress, String deviceType, String failureReason, String loginInfo) {
        LoginLog loginLog = LoginLog.builder()
                .userId(userId)
                .ipAddress(ipAddress)
                .deviceType(deviceType)
                .status("FAILED")
                .failureReason(failureReason)
                .createdAt(LocalDateTime.now())
                .loginInfo(loginInfo)
                .build();
        
        return loginLogRepository.save(loginLog);
    }

    /**
     * 获取用户的登录历史
     */
    @Override
    @Transactional(readOnly = true)
    public List<LoginLog> getUserLoginHistory(String userId) {
        return loginLogRepository.findByUserId(userId);
    }
    
    /**
     * 分页获取用户的登录历史
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LoginLog> getUserLoginHistory(String userId, Pageable pageable) {
        return loginLogRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }
    
    /**
     * 检查用户是否被锁定（短时间内多次登录失败）
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isUserLocked(String userId, int lockThreshold, int minutes) {
        LocalDateTime timeAgo = LocalDateTime.now().minusMinutes(minutes);
        long failedAttempts = loginLogRepository.countRecentFailedAttempts(userId, timeAgo);
        return failedAttempts >= lockThreshold;
    }
    
    /**
     * 获取可疑的登录活动
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getSuspiciousLoginActivities(int threshold, int minutes) {
        LocalDateTime timeAgo = LocalDateTime.now().minusMinutes(minutes);
        List<Object[]> suspiciousIPs = loginLogRepository.findSuspiciousIPs(timeAgo, threshold);
        
        Map<String, Long> result = new HashMap<>();
        for (Object[] row : suspiciousIPs) {
            String ipAddress = (String) row[0];
            Long count = (Long) row[1];
            result.put(ipAddress, count);
        }
        
        return result;
    }
    
    /**
     * 清理旧的登录日志
     */
    @Override
    @Transactional
    public long cleanupOldLogs(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        List<LoginLog> oldLogs = loginLogRepository.findByCreatedAtBetween(
            LocalDateTime.MIN, cutoffDate);
        
        long count = oldLogs.size();
        loginLogRepository.deleteAll(oldLogs);
        return count;
    }
}
