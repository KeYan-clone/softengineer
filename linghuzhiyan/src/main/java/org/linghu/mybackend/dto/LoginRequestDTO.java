package org.linghu.mybackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
    
    /**
     * 用户角色标识，用于选择身份登录
     * 可选值: ADMIN/TEACHER/ASSISTANT/STUDENT 或 ROLE_ADMIN/ROLE_TEACHER/ROLE_ASSISTANT/ROLE_STUDENT
     */
    private String role;
}
