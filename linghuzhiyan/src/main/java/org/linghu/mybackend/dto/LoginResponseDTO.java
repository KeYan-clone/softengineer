package org.linghu.mybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 登录响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    
    /**
     * 用户信息
     */
    private UserDTO user;
    
    /**
     * JWT token
     */
    private String token;
    
    /**
     * token类型
     */
    private String tokenType;
    
    /**
     * token过期时间（秒）
     */
    private long expiresIn;
}
