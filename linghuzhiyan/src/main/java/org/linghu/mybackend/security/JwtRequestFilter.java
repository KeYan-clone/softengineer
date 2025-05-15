package org.linghu.mybackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.linghu.mybackend.service.LoginLogService;
import org.linghu.mybackend.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器，用于验证请求中的JWT令牌
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsServiceImpl userDetailsService;
    
    private final LoginLogService loginLogService;

     // JWT 令牌请求头
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    // JWT 令牌前缀
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil, UserDetailsServiceImpl userDetailsService, LoginLogService loginLogService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.loginLogService = loginLogService;
    }    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(this.tokenHeader);

        String username = null;
        String jwt = null;
        String ipAddress = RequestUtils.getClientIpAddress(request);
        String deviceType = RequestUtils.getDeviceType(request);
        String loginInfo = RequestUtils.collectRequestInfo(request);

        // 从请求头中提取JWT令牌
        if (authorizationHeader != null && authorizationHeader.startsWith(this.tokenHead)) {
            jwt = authorizationHeader.substring(this.tokenHead.length());
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwt);
            } catch (Exception e) { // 令牌解析失败
                logger.warn("无法解析JWT令牌", e);
                // 记录令牌解析失败的日志
                if (username != null) {
                    loginLogService.logFailedLogin(
                        username,
                        ipAddress,
                        deviceType,
                        "JWT令牌解析失败: " + e.getMessage(),
                        loginInfo
                    );
                }
            }
        }

        // 如果找到用户名且当前上下文中没有认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 验证令牌是否有效
                if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 更新SecurityContext中的认证信息
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.debug("已认证用户 " + username + ", 设置安全上下文");
                    
                    // 记录令牌验证成功的日志（可选，视需求而定）
                    // 注意：为避免每次API请求都记录日志，可以只针对特定路径或跳过某些常用API路径
                    // 这里仅针对登录相关API记录日志
                    String requestURI = request.getRequestURI();
                    if (requestURI.contains("/api/auth/") && !requestURI.contains("/api/auth/login")) {
                        loginLogService.logSuccessfulLogin(
                            username,
                            ipAddress,
                            deviceType,
                            loginInfo
                        );
                    }
                } else {
                    // 记录令牌验证失败的日志
                    loginLogService.logFailedLogin(
                        username,
                        ipAddress,
                        deviceType,
                        "JWT令牌验证失败",
                        loginInfo
                    );
                }
            } catch (UsernameNotFoundException e) {
                logger.warn("用户名不存在: " + e.getMessage());
                // 记录用户名不存在的日志
                loginLogService.logFailedLogin(
                    username,
                    ipAddress,
                    deviceType,
                    "用户名不存在: " + e.getMessage(),
                    loginInfo
                );
            }
        }

        chain.doFilter(request, response);
    }
}
