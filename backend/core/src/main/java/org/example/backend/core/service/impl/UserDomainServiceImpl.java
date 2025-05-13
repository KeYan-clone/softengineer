package org.example.backend.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.backend.core.domain.User;
import org.example.backend.core.domain.UserRole;
import org.example.backend.core.repository.UserRepository;
import org.example.backend.core.service.UserDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * User Domain Service implementation
 * Handles core domain operations for user entities
 */
@Service
@RequiredArgsConstructor
public class UserDomainServiceImpl implements UserDomainService {
    
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public User createUser(User user) {
        // Set creation and update time
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        
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
        // Set update time
        user.setUpdateTime(new Date());
        
        return userRepository.save(user);
    }
    
    @Override
    @Transactional
    public void deleteUser(String id) {
        userRepository.deleteById(id);
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
    public List<User> findByRole(UserRole role) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == role)
                .toList();
    }
}
