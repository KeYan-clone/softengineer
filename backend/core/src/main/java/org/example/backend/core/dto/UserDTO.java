package org.example.backend.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.core.domain.UserRole;

/**
 * 用户数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private String id;
    private String username;
    private String email;
    private UserRole role;
    private String createTime;
    private String updateTime;
}
