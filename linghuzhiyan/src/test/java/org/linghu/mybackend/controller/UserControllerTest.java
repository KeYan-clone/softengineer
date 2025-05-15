package org.linghu.mybackend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.linghu.mybackend.dto.LoginRequestDTO;
import org.linghu.mybackend.dto.LoginResponseDTO;
import org.linghu.mybackend.dto.UserDTO;
import org.linghu.mybackend.exception.UserException;
import org.linghu.mybackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequestDTO validLoginRequest;
    private LoginRequestDTO adminRoleLoginRequest;
    private LoginRequestDTO invalidRoleLoginRequest;
    private LoginResponseDTO successResponse;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        userDTO = new UserDTO();
        userDTO.setId("1");
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_STUDENT");
        userDTO.setRoles(roles);

        validLoginRequest = new LoginRequestDTO();
        validLoginRequest.setUsername("testuser");
        validLoginRequest.setPassword("password123");
        // 不设置角色

        adminRoleLoginRequest = new LoginRequestDTO();
        adminRoleLoginRequest.setUsername("testuser");
        adminRoleLoginRequest.setPassword("password123");
        adminRoleLoginRequest.setRole("ADMIN");

        invalidRoleLoginRequest = new LoginRequestDTO();
        invalidRoleLoginRequest.setUsername("testuser");
        invalidRoleLoginRequest.setPassword("password123");
        invalidRoleLoginRequest.setRole("TEACHER");

        successResponse = LoginResponseDTO.builder()
                .user(userDTO)
                .token("jwt-token")
                .tokenType("Bearer")
                .expiresIn(3600)
                .build();
    }

    @Test
    void shouldLoginSuccessfullyWhenNoRoleSpecified() throws Exception {
        // 配置模拟服务响应
        when(userService.login(any(LoginRequestDTO.class))).thenReturn(successResponse);

        // 执行登录请求
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("jwt-token"))
                .andExpect(jsonPath("$.data.user.username").value("testuser"));
    }

    @Test
    void shouldFailLoginWhenRoleNotAuthorized() throws Exception {
        // 配置模拟服务响应
        when(userService.login(any(LoginRequestDTO.class)))
                .thenThrow(UserException.roleNotAuthorized());

        // 执行登录请求
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRoleLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(100006))
                .andExpect(jsonPath("$.message").value("您没有该身份的权限，请选择其他身份登录"));
    }
}
