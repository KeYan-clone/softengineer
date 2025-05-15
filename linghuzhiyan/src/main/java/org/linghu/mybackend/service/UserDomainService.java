package org.linghu.mybackend.service;

import org.linghu.mybackend.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 用户领域服务接口
 * 定义与用户相关的核心领域操作，
 * 专注于数据操作
 */
public interface UserDomainService {
      /**
     * 创建新用户
     * 
     * @param user 要创建的用户
     * @return 创建的用户
     */
    User createUser(User user);
      /**
     * 根据ID查找用户
     * 
     * @param id 用户ID
     * @return 用户可选对象
     */
    Optional<User> findById(String id);
      /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 用户可选对象
     */
    Optional<User> findByUsername(String username);
      /**
     * 根据邮箱查找用户
     * 
     * @param email 邮箱
     * @return 用户可选对象
     */
    Optional<User> findByEmail(String email);
      /**
     * 检查用户名是否已存在
     * 
     * @param username 用户名
     * @return 如果存在则返回true
     */
    boolean existsByUsername(String username);
      /**
     * 检查邮箱是否已存在
     * 
     * @param email 邮箱
     * @return 如果存在则返回true
     */
    boolean existsByEmail(String email);
      /**
     * 更新用户
     * 
     * @param user 要更新的用户
     * @return 更新后的用户
     */
    User updateUser(User user);
      /**
     * 根据ID删除用户
     * 
     * @param id 用户ID
     */
    void deleteUser(String id);
      /**
     * 查找所有用户
     * 
     * @return 用户列表
     */
    List<User> findAll();
      /**
     * 分页查找所有用户
     * 
     * @param pageable 分页信息
     * @return 用户分页对象
     */
    Page<User> findAll(Pageable pageable);    /**
     * 根据角色ID查找用户
     * 
     * @param roleId 角色ID
     * @return 用户列表
     */
    List<User> findByRoleId(String roleId);
      /**
     * 给用户分配角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 更新后的用户
     */
    User assignRoleToUser(String userId, String roleId);
      /**
     * 从用户中移除角色
     * 
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 更新后的用户
     */
    User removeRoleFromUser(String userId, String roleId);
      /**
     * 获取用户角色
     * 
     * @param userId 用户ID
     * @return 角色ID集合
     */
    Set<String> getUserRoleIds(String userId);
}
