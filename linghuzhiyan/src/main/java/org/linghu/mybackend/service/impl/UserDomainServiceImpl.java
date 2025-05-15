package org.linghu.mybackend.service.impl;

import lombok.RequiredArgsConstructor;

import org.linghu.mybackend.domain.Role;
import org.linghu.mybackend.domain.User;
import org.linghu.mybackend.domain.UserRoleId;
import org.linghu.mybackend.domain.UserRoleRelation;
import org.linghu.mybackend.repository.RoleRepository;
import org.linghu.mybackend.repository.UserRepository;
import org.linghu.mybackend.repository.UserRoleRepository;
import org.linghu.mybackend.service.UserDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User Domain Service implementation
 * Handles core domain operations for user entities
 */
@Service
@RequiredArgsConstructor
public class UserDomainServiceImpl implements UserDomainService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public User createUser(User user) {
        Date now = new Date();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setIsDeleted(false);

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        user.setUpdatedAt(new Date());

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setIsDeleted(true);
        user.setUpdatedAt(new Date());
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByRoleId(String roleId) {
        List<UserRoleRelation> userRoles = userRoleRepository.findByIdRoleId(roleId);
        Set<String> userIds = userRoles.stream()
                .map(ur -> ur.getId().getUserId())
                .collect(Collectors.toSet());

        return userRepository.findAllById(userIds);
    }

    @Override
    @Transactional
    public User assignRoleToUser(String userId, String roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Check if user already has this role
        UserRoleId userRoleId = new UserRoleId(userId, roleId);
        if (userRoleRepository.existsById(userRoleId)) {
            return user;
        }

        // 创建新的用户角色关联，不再设置对象引用
        UserRoleRelation userRole = new UserRoleRelation(userId, roleId);

        // 保存关联
        userRoleRepository.save(userRole);
        return user;
    }

    @Override
    @Transactional
    public User removeRoleFromUser(String userId, String roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Delete user-role relationship if it exists
        UserRoleId userRoleId = new UserRoleId(userId, roleId);
        userRoleRepository.findById(userRoleId)
                .ifPresent(userRoleRepository::delete);

        // Clear and refresh the user to avoid ConcurrentModificationException
        user = userRepository.findById(userId).orElse(user);

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getUserRoleIds(String userId) {
        List<UserRoleRelation> userRoles = userRoleRepository.findByIdUserId(userId);
        return userRoles.stream()
                .map(ur -> ur.getId().getRoleId())
                .collect(Collectors.toSet());
    }
}
