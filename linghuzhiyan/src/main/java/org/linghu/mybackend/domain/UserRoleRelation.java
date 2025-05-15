package org.linghu.mybackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户角色关联实体，对应数据库中的用户角色关联表
 * 根据新的数据库设计，移除了外键约束
 */
@Entity
@Table(name = "user_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRelation {

    @EmbeddedId
    private UserRoleId id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
    
    public UserRoleRelation(String userId, String roleId) {
        this.id = new UserRoleId(userId, roleId);
        this.createdAt = new Date();
    }

}
