package org.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.core.dto.UserDTO;

/**
 * 用户登录响应DTO，包含用户信息和JWT令牌
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    
    private UserDTO user;
    private String token;
}
