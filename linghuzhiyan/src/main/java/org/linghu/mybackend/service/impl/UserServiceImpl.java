package org.linghu.mybackend.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.linghu.mybackend.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.linghu.mybackend.dto.LoginRequestDTO;
import org.linghu.mybackend.dto.LoginResponseDTO;
import org.linghu.mybackend.dto.ProfileUpdateDTO;
import org.linghu.mybackend.dto.UserDTO;
import org.linghu.mybackend.dto.UserRegistrationDTO;
import org.linghu.mybackend.exception.UserException;
import org.linghu.mybackend.security.JwtTokenUtil;
import org.linghu.mybackend.service.LoginLogService;
import org.linghu.mybackend.service.UserDomainService;
import org.linghu.mybackend.service.UserService;
import org.linghu.mybackend.constants.SystemConstants;
import org.linghu.mybackend.utils.JsonUtils;
import org.linghu.mybackend.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户服务实现类
 * 实现业务层面的用户操作，依赖于UserDomainService处理核心领域操作
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDomainService userDomainService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final LoginLogService loginLogService;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Value("${jwt.expiration}")
    private long expiration;

    @Override
    @Transactional
    public UserDTO registerUser(UserRegistrationDTO registrationDTO) { // 检查用户名是否已存在
        if (userDomainService.existsByUsername(registrationDTO.getUsername())) {
            throw UserException.usernameAlreadyExists();
        }

        // 检查邮箱是否已存在
        if (userDomainService.existsByEmail(registrationDTO.getEmail())) {
            throw UserException.emailAlreadyExists();
        }

        // 创建用户实体
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword())); // 对密码进行加密
        // 使用新的standardizeProfile方法处理profile
        user.setProfile(JsonUtils.standardizeProfile("{}"));

        // 保存用户
        User savedUser = userDomainService.createUser(user);

        // 默认分配学生角色
        userDomainService.assignRoleToUser(savedUser.getId(), SystemConstants.ROLE_STUDENT);
        //
        return convertToDTO(savedUser);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {        // 获取当前请求信息
        String ipAddress = "unknown";
        String deviceType = "unknown";
        String loginInfo = "{}";
        
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            ipAddress = RequestUtils.getClientIpAddress(request);
            deviceType = RequestUtils.getDeviceType(request);
            loginInfo = RequestUtils.collectRequestInfo(request);
        }
        
        try {
            // 使用Spring Security进行身份验证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(),
                            loginRequestDTO.getPassword()));

            // 获取认证成功的用户详情
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 查找用户信息
            User user = userDomainService.findByUsername(userDetails.getUsername())
                    .orElseThrow(UserException::userNotFound);

            // 如果指定了角色，验证用户是否拥有该角色
            if (loginRequestDTO.getRole() != null && !loginRequestDTO.getRole().isEmpty()) {
                // 获取用户的所有角色ID
                Set<String> userRoles = userDomainService.getUserRoleIds(user.getId());

                // 检查用户是否拥有指定角色，考虑角色前缀
                String roleWithPrefix = loginRequestDTO.getRole();
                if (!roleWithPrefix.startsWith("ROLE_")) {
                    roleWithPrefix = "ROLE_" + roleWithPrefix;
                }

                if (!userRoles.contains(roleWithPrefix)) {
                    // 记录角色授权失败日志
                    loginLogService.logFailedLogin(
                        user.getId(),
                        ipAddress,
                        deviceType,
                        "角色未授权: " + loginRequestDTO.getRole(),
                        loginInfo
                    );
                    throw UserException.roleNotAuthorized();
                }
            }

            // 记录登录成功日志
            loginLogService.logSuccessfulLogin(
                user.getId(),
                ipAddress,
                deviceType,
                loginInfo
            );

            // 生成JWT token
            String token = jwtTokenUtil.generateToken(userDetails);

            // 构建响应
            return LoginResponseDTO.builder()
                    .user(convertToDTO(user))
                    .token(token)
                    .tokenType(tokenHead)
                    .expiresIn(expiration / 1000) // 转换为秒
                    .build();
        } catch (BadCredentialsException e) {
            // 记录密码错误日志（注意不能泄露敏感信息）
            loginLogService.logFailedLogin(
                loginRequestDTO.getUsername(), // 这里只记录用户名，因为用户不存在
                ipAddress,
                deviceType,
                "密码错误",
                loginInfo
            );
            throw UserException.invalidCredentials();
        } catch (Exception e) {
            // 记录其他登录失败情况
            loginLogService.logFailedLogin(
                loginRequestDTO.getUsername(),
                ipAddress,
                deviceType,
                "登录失败: " + e.getMessage(),
                loginInfo
            );
            throw e;
        }
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
        // 检查用户是否存在
        User user = userDomainService.findById(id)
                .orElseThrow(UserException::userNotFound);

        // 如果用户名有更改，检查新用户名是否已存在
        if (!user.getUsername().equals(userDTO.getUsername()) &&
                userDomainService.existsByUsername(userDTO.getUsername())) {
            throw UserException.usernameAlreadyExists();
        }

        // 如果邮箱有更改，检查新邮箱是否已存在
        if (!user.getEmail().equals(userDTO.getEmail()) &&
                userDomainService.existsByEmail(userDTO.getEmail())) {
            throw UserException.emailAlreadyExists();
        } 
        // 更新用户基本信息
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setAvatar(userDTO.getAvatar());
        // 使用standardizeProfile方法处理profile
        user.setProfile(JsonUtils.standardizeProfile(userDTO.getProfile()));

        // 更新用户
        User updatedUser = userDomainService.updateUser(user);

        // 处理角色更新
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            // 获取当前用户角色
            Set<String> currentRoleIds = userDomainService.getUserRoleIds(id);

            // 需要添加的角色
            Set<String> rolesToAdd = new HashSet<>(userDTO.getRoles());
            rolesToAdd.removeAll(currentRoleIds);

            // 需要移除的角色
            Set<String> rolesToRemove = new HashSet<>(currentRoleIds);
            rolesToRemove.removeAll(userDTO.getRoles());

            // 添加新角色
            for (String roleId : rolesToAdd) {
                userDomainService.assignRoleToUser(id, roleId);
            }

            // 移除旧角色
            for (String roleId : rolesToRemove) {
                userDomainService.removeRoleFromUser(id, roleId);
            }
        }

        return convertToDTO(updatedUser);
    }

    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        // 验证用户存在
        User user = userDomainService.findByUsername(username)
                .orElseThrow(UserException::userNotFound);

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw UserException.invalidOldPassword();
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userDomainService.updateUser(user);
    }

    @Override
    public Page<UserDTO> listUsers(int pageNum, int pageSize) {
        // 页码从0开始计算
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<User> userPage = userDomainService.findAll(pageable);

        // 转换为DTO
        return userPage.map(this::convertToDTO);
    }

    /**
     * 将User实体转换为UserDTO
     * 
     * @param user 用户实体
     * @return 用户DTO
     */
    private UserDTO convertToDTO(User user) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAvatar(user.getAvatar());
        dto.setProfile(JsonUtils.parseObject(user.getProfile(), Object.class));
        dto.setRoles(userDomainService.getUserRoleIds(user.getId()));
        dto.setCreatedAt(dateFormat.format(user.getCreatedAt()));
        dto.setUpdatedAt(dateFormat.format(user.getUpdatedAt()));
        dto.setIsDeleted(user.getIsDeleted());

        return dto;
    }

    @Override
    @Transactional
    public UserDTO updateUserProfile(String username, ProfileUpdateDTO profileUpdateDTO) {
        // 检查用户是否存在
        User user = userDomainService.findByUsername(username)
                .orElseThrow(UserException::userNotFound);

        // 只更新允许的字段：头像和个人资料
        if (profileUpdateDTO.getAvatar() != null) {
            user.setAvatar(profileUpdateDTO.getAvatar());
        }

        if (profileUpdateDTO.getProfile() != null) {
            // 将Object类型的profile转换为JSON字符串，然后进行标准化处理
            String profileJson = JsonUtils.toJsonString(profileUpdateDTO.getProfile());
            user.setProfile(JsonUtils.standardizeProfile(profileJson));
        }

        // 更新用户
        User updatedUser = userDomainService.updateUser(user);

        // 转换为DTO返回
        return convertToDTO(updatedUser);
    }
    
    @Override
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("User not authenticated");
        }
        return authentication.getName(); // 假设用户名就是用户ID
    }
    
    @Override
    public UserDTO getUserInfo(String userId) {
        return getUserById(userId);
    }
}
