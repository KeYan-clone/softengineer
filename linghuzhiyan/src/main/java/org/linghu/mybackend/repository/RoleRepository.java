package org.linghu.mybackend.repository;

import org.linghu.mybackend.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 角色仓储接口，负责角色数据的持久化
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    
    /**
     * 根据角色名称查找角色
     *
     * @param name 角色名称
     * @return 角色可选项
     */
    Optional<Role> findByName(String name);
    
    /**
     * 检查角色名称是否已存在
     *
     * @param name 角色名称
     * @return 如果存在返回true，否则返回false
     */
    boolean existsByName(String name);
}
