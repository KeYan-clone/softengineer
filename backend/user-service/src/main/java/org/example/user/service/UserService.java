package org.example.user.service;

import org.example.user.dto.LoginRequestDTO;
import org.example.user.dto.LoginResponseDTO;
import org.example.backend.core.dto.UserDTO;
import org.example.user.dto.UserRegistrationDTO;
import org.springframework.data.domain.Page;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 注册新用户
     * 
     * @param registrationDTO 用户注册信息
     * @return 注册成功的用户信息
     */
    UserDTO registerUser(UserRegistrationDTO registrationDTO);
    
    /**
     * 用户登录
     * 
     * @param loginRequestDTO 登录信息
     * @return 登录响应，包含用户信息和令牌
     */
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    
    /**
     * 根据ID获取用户信息
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    UserDTO getUserById(String id);
    
    /**
     * 根据用户名获取用户信息
     * 
     * @param username 用户名
     * @return 用户信息
     */
    UserDTO getUserByUsername(String username);
    
    /**
     * 更新用户信息
     * 
     * @param id 用户ID
     * @param userDTO 更新的用户信息
     * @return 更新后的用户信息
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
     * 分页获取用户列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 用户分页结果
     */
    Page<UserDTO> listUsers(int pageNum, int pageSize);
}
