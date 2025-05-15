package org.linghu.mybackend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户角色关联表的复合主键
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleId implements Serializable {

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "role_id", nullable = false, length = 20)
    private String roleId;
}
