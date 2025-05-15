package org.linghu.mybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Size;

/**
 * 用户资料更新数据传输对象
 * 只包含允许用户更新的字段
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateDTO {
    
    @Size(max = 255, message = "头像URL不能超过255个字符")
    private String avatar;
    
    /**
     * JSON格式的个人资料
     * 可以包含用户的额外信息，如性别、年龄、兴趣爱好等
     */
    private Object profile; 
    
    // 可以根据需要添加其他允许用户更新的字段
    // 敏感字段如username、email、password和roles不应包含在此
}
