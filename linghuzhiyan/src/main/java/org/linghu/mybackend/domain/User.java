package org.linghu.mybackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户领域模型，对应数据库中的用户表
 */
@Entity
@Table(name = "users")
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 255)
    private String avatar;

    @Column(columnDefinition = "json")
    private String profile;

    // //todo : fix get roles; now it is always null
    // @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    // private Set<UserRoleRelation> userRoles = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    } // Convenience method to add a role to user safely

    // public void addRole(UserRoleRelation userRole) {
    //     // Create a new set to avoid concurrent modification
    //     Set<UserRoleRelation> updatedRoles = new HashSet<>(userRoles);
    //     updatedRoles.add(userRole);
    //     this.userRoles = updatedRoles;
    // }

    // Convenience method to remove a role from user safely
    // public void removeRole(String roleId) {
    //     // Create a new set to avoid concurrent modification
    //     Set<UserRoleRelation> updatedRoles = new HashSet<>();
    //     for (UserRoleRelation role : userRoles) {
    //         if (!role.getId().getRoleId().equals(roleId)) {
    //             updatedRoles.add(role);
    //         }
    //     }
    //     this.userRoles = updatedRoles;
    // }

    // Check if user has a specific role
    // public boolean hasRole(String roleId) {
    //     if (userRoles == null)
    //         return false;

    //     for (UserRoleRelation role : userRoles) {
    //         if (role != null && role.getId() != null &&
    //                 roleId.equals(role.getId().getRoleId())) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }

}
