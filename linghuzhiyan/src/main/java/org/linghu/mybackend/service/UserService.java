package org.linghu.mybackend.service;

import org.linghu.mybackend.dto.LoginRequestDTO;
import org.linghu.mybackend.dto.LoginResponseDTO;
import org.linghu.mybackend.dto.ProfileUpdateDTO;
import org.linghu.mybackend.dto.UserDTO;
import org.linghu.mybackend.dto.UserRegistrationDTO;
import org.springframework.data.domain.Page;

/**
 * 用户服务接口
 * 定义与用户相关的业务操作
 */
public interface UserService {
    
    /**
     * 用户注册
     * 
     * @param registrationDTO 注册信息
     * @return 用户DTO
     */
    UserDTO registerUser(UserRegistrationDTO registrationDTO);
    
    /**
     * 用户登录
     * 
     * @param loginRequestDTO 登录信息
     * @return 登录响应
     */
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    
    /**
     * 根据ID获取用户
     * 
     * @param id 用户ID
     * @return 用户DTO
     */
    UserDTO getUserById(String id);
    
    /**
     * 根据用户名获取用户
     * 
     * @param username 用户名
     * @return 用户DTO
     */
    UserDTO getUserByUsername(String username);
    
    /**
     * 更新用户
     * 
     * @param id 用户ID
     * @param userDTO 用户信息
     * @return 更新后的用户DTO
     */
    UserDTO updateUser(String id, UserDTO userDTO);
    
    /**
     * 修改密码
     * 
     * @param username 用户名
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(String username, String oldPassword, String newPassword);
      /**
     * 分页查询用户
     * 
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 用户分页列表
     */
    Page<UserDTO> listUsers(int pageNum, int pageSize);
    
    /**
     * 更新用户资料
     * 
     * @param username 用户名
     * @param profileUpdateDTO 用户资料更新信息
     * @return 更新后的用户DTO
     */
    UserDTO updateUserProfile(String username, ProfileUpdateDTO profileUpdateDTO);
    
    /**
     * 获取当前登录用户的ID
     * 
     * @return 当前用户ID
     */
    String getCurrentUserId();
      /**
     * 获取简要用户信息
     * 
     * @param userId 用户ID
     * @return 用户信息DTO
     */
    UserDTO getUserInfo(String userId);
}
