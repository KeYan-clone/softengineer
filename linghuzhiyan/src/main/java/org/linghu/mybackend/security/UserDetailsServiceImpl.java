package org.linghu.mybackend.security;

import org.linghu.mybackend.domain.User;
import org.linghu.mybackend.service.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
/**
 * 自定义用户详情服务实现
 * 负责从数据库中加载用户信息及权限
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDomainService userDomainService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDomainService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        // 检查账户状态
        if (user.getIsDeleted()) {
            throw new UsernameNotFoundException("用户已被删除: " + username);
        }

        Set<String> roleIds = userDomainService.getUserRoleIds(user.getId());

        // 返回Spring Security的UserDetails对象
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String roleId : roleIds) {
            authorities.add(new SimpleGrantedAuthority(roleId));
        }
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // 应该是加密后的密码
                .authorities(authorities)
                .disabled(user.getIsDeleted()) // 是否禁用
                .accountExpired(false) // 账户是否过期
                .accountLocked(false) // 账户是否锁定
                .credentialsExpired(false) // 凭证是否过期
                .build();
    }
}
