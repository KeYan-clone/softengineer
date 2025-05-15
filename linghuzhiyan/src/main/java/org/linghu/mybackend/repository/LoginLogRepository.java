package org.linghu.mybackend.repository;

import org.linghu.mybackend.domain.LoginLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 登录日志的数据访问层接口
 */
@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
    
    /**
     * 根据用户ID查找登录日志
     * @param userId 用户ID
     * @return 用户的登录日志列表
     */
    List<LoginLog> findByUserId(String userId);
    
    /**
     * 根据用户ID和状态查找登录日志（分页）
     * @param userId 用户ID
     * @param status 登录状态
     * @param pageable 分页信息
     * @return 符合条件的登录日志分页
     */
    Page<LoginLog> findByUserIdAndStatus(String userId, String status, Pageable pageable);
    
    /**
     * 查找某个时间段内的登录日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 该时间段内的登录日志列表
     */
    List<LoginLog> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根据IP地址查找登录日志
     * @param ipAddress IP地址
     * @return 来自指定IP的登录日志列表
     */
    List<LoginLog> findByIpAddress(String ipAddress);
    
    /**
     * 查询最近的失败登录尝试次数
     * @param userId 用户ID
     * @param minutes 过去的分钟数
     * @return 失败尝试次数
     */
    @Query("SELECT COUNT(l) FROM LoginLog l WHERE l.userId = :userId AND l.status = 'FAILED' AND l.createdAt >= :timeAgo")
    long countRecentFailedAttempts(@Param("userId") String userId, @Param("timeAgo") LocalDateTime timeAgo);
    
    /**
     * 按时间倒序查询用户的登录日志
     * @param userId 用户ID
     * @param pageable 分页信息
     * @return 分页的登录日志
     */
    Page<LoginLog> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
    
    /**
     * 查询可疑的登录行为（短时间内多次失败的登录）
     * @param ipAddress IP地址
     * @param timeAgo 时间范围
     * @param threshold 失败阈值
     * @return 可疑的IP地址列表
     */
    @Query("SELECT l.ipAddress, COUNT(l) as attempts FROM LoginLog l WHERE l.status = 'FAILED' AND l.createdAt >= :timeAgo GROUP BY l.ipAddress HAVING COUNT(l) >= :threshold")
    List<Object[]> findSuspiciousIPs(@Param("timeAgo") LocalDateTime timeAgo, @Param("threshold") long threshold);
}
