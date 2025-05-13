package org.example.user.service.impl;

import org.example.backend.core.domain.User;
import org.example.backend.common.security.JwtTokenUtil;
import org.example.backend.core.service.UserDomainService;

import org.example.user.dto.LoginRequestDTO;
import org.example.user.dto.LoginResponseDTO;
import org.example.backend.core.dto.UserDTO;
import org.example.user.dto.UserRegistrationDTO;
import org.example.backend.common.exception.UserException;
import org.example.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户服务实现类
 * 实现业务层面的用户操作，依赖于UserDomainService处理核心领域操作
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDomainService userDomainService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public UserServiceImpl(UserDomainService userDomainService, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil) {
        this.userDomainService = userDomainService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    @Transactional
    public UserDTO registerUser(UserRegistrationDTO registrationDTO) {
        // 检查用户名是否已存在
        if (userDomainService.existsByUsername(registrationDTO.getUsername())) {
            throw UserException.usernameAlreadyExists();
        }

        // 检查邮箱是否已存在
        if (userDomainService.existsByEmail(registrationDTO.getEmail())) {
            throw UserException.emailAlreadyExists();
        }

        // 创建新用户
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(registrationDTO.getRole());

        // 使用领域服务保存用户并返回
        User savedUser = userDomainService.createUser(user);
        return convertToDTO(savedUser);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        try {
            // 验证用户名和密码
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),
                            loginRequestDTO.getPassword()));
        } catch (BadCredentialsException e) {
            throw UserException.invalidCredentials();
        }
        // 加载用户详情并生成JWT令牌
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // 获取用户信息
        User user = userDomainService.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(UserException::userNotFound);

        // 返回登录响应
        return new LoginResponseDTO(convertToDTO(user), token);
    }

    @Override
    public UserDTO getUserById(String id) {
        User user = userDomainService.findById(id)
                .orElseThrow(UserException::userNotFound);
        return convertToDTO(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userDomainService.findByUsername(username)
                .orElseThrow(UserException::userNotFound);
        return convertToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(String id, UserDTO userDTO) {
        User user = userDomainService.findById(id)
                .orElseThrow(UserException::userNotFound);

        // 如果更新用户名，检查是否与其他用户冲突
        if (!user.getUsername().equals(userDTO.getUsername()) &&
                userDomainService.existsByUsername(userDTO.getUsername())) {
            throw UserException.usernameAlreadyExists();
        }

        // 如果更新邮箱，检查是否与其他用户冲突
        if (!user.getEmail().equals(userDTO.getEmail()) &&
                userDomainService.existsByEmail(userDTO.getEmail())) {
            throw UserException.emailAlreadyExists();
        }

        // 更新用户信息
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        }

        // 保存并返回更新后的用户
        User updatedUser = userDomainService.updateUser(user);
        return convertToDTO(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userDomainService.findByUsername(username)
                .orElseThrow(UserException::userNotFound);

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw UserException.invalidCredentials();
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userDomainService.updateUser(user);
    }

    @Override
    public Page<UserDTO> listUsers(int pageNum, int pageSize) {
        Page<User> userPage = userDomainService.findAll(PageRequest.of(pageNum - 1, pageSize));
        return userPage.map(this::convertToDTO);
    }

    /**
     * 将User实体转换为UserDTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreateTime(formatDate(user.getCreateTime()));
        dto.setUpdateTime(formatDate(user.getUpdateTime()));
        return dto;
    }

    /**
     * 格式化日期
     */
    private String formatDate(Date date) {
        return date != null ? dateFormat.format(date) : null;
    }
}
