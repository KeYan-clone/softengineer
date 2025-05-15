package org.linghu.mybackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.linghu.mybackend.service.LoginLogService;
import org.linghu.mybackend.utils.RequestUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT访问被拒处理器 - 处理已认证但权限不足的情况（403）
 */
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    
    private final LoginLogService loginLogService;    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        // 1. 设置编码和内容类型
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
        
        // 2. 准备错误响应
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("status", 403);
        errorData.put("message", "权限不足，无法访问");
        errorData.put("path", request.getRequestURI());
        errorData.put("error", accessDeniedException.getMessage());
        
        // 3. 记录权限不足的登录日志
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = "未知用户";
            if (authentication != null && authentication.getName() != null) {
                username = authentication.getName();
            }
            
            String ipAddress = RequestUtils.getClientIpAddress(request);
            String deviceType = RequestUtils.getDeviceType(request);
            String loginInfo = RequestUtils.collectRequestInfo(request);
            
            loginLogService.logFailedLogin(
                username,
                ipAddress,
                deviceType,
                "权限不足: " + request.getRequestURI(),
                loginInfo
            );
        } catch (Exception e) {
            // 记录日志时发生异常，不影响正常的错误响应
            e.printStackTrace();
        }
        
        // 4. 使用ObjectMapper将错误信息转换为JSON并写入响应
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorData);
    }
}
