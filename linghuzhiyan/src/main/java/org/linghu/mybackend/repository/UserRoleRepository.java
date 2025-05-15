package org.linghu.mybackend.repository;

import org.linghu.mybackend.domain.UserRoleId;
import org.linghu.mybackend.domain.UserRoleRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 用户角色关联仓储接口，负责用户角色关联数据的持久化
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleRelation, UserRoleId> {
    
    /**
     * 根据用户ID查找用户角色关联
     *
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    List<UserRoleRelation> findByIdUserId(String userId);
    
    /**
     * 根据角色ID查找用户角色关联
     *
     * @param roleId 角色ID
     * @return 用户角色关联列表
     */
    List<UserRoleRelation> findByIdRoleId(String roleId);
    
    /**
     * 根据用户ID删除所有用户角色关联
     *
     * @param userId 用户ID
     */
    void deleteByIdUserId(String userId);
    
    /**
     * 根据用户ID和角色ID集合检查是否存在关联
     *
     * @param userId 用户ID
     * @param roleIds 角色ID集合
     * @return 存在返回true，否则返回false
     */
    boolean existsByIdUserIdAndIdRoleIdIn(String userId, Set<String> roleIds);
}
