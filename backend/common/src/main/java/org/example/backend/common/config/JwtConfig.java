package org.example.backend.common.config;

import org.example.backend.common.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JWT 配置类
 * 其他微服务可以导入此配置
 */
@Configuration
public class JwtConfig {
    
    @Value("${jwt.secret:linghu-default-secret-key-must-be-at-least-32-chars}")
    private String secret;

    @Value("${jwt.expiration:3600000}")
    private long expiration;
    
    /**
     * 创建 JwtTokenUtil Bean
     */
    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        jwtTokenUtil.setSecret(secret);
        jwtTokenUtil.setExpiration(expiration);
        return jwtTokenUtil;
    }
}
