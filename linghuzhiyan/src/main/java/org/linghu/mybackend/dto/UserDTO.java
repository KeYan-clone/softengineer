package org.linghu.mybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

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
    private String avatar;
    private Object profile; 
    private Set<String> roles; 
    private String createdAt;
    private String updatedAt;
    private Boolean isDeleted;
}
