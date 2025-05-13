package org.example.backend.core.service;

import org.example.backend.core.domain.User;
import org.example.backend.core.domain.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * User Domain Service interface 
 * Defines core domain operations related to users,
 * focusing on data operations rather than business logic
 */
public interface UserDomainService {
    
    /**
     * Create a new user
     * 
     * @param user user to create
     * @return created user
     */
    User createUser(User user);
    
    /**
     * Find user by ID
     * 
     * @param id user ID
     * @return user optional
     */
    Optional<User> findById(String id);
    
    /**
     * Find user by username
     * 
     * @param username username
     * @return user optional
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     * 
     * @param email email
     * @return user optional
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if username already exists
     * 
     * @param username username
     * @return true if exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email already exists
     * 
     * @param email email
     * @return true if exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Update user
     * 
     * @param user user to update
     * @return updated user
     */
    User updateUser(User user);
    
    /**
     * Delete user by ID
     * 
     * @param id user ID
     */
    void deleteUser(String id);
    
    /**
     * Find all users
     * 
     * @return list of users
     */
    List<User> findAll();
    
    /**
     * Find all users with pagination
     * 
     * @param pageable pagination info
     * @return page of users
     */
    Page<User> findAll(Pageable pageable);
    
    /**
     * Find users by role
     * 
     * @param role user role
     * @return list of users
     */
    List<User> findByRole(UserRole role);
}
