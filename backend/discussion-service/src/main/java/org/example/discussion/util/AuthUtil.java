package org.example.discussion.util;

import org.example.discussion.dto.UserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for authentication-related operations
 */
public class AuthUtil {
    
    private AuthUtil() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Get the current authenticated user
     *
     * @return UserDTO of the current user
     * @throws RuntimeException If no user is authenticated
     */
    public static UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDTO) {
            return (UserDTO) authentication.getPrincipal();
        }
        throw new RuntimeException("User not authenticated");
    }
    
    /**
     * Check if the current user is authenticated
     *
     * @return true if authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && 
               authentication.getPrincipal() instanceof UserDTO;
    }
    
    /**
     * Check if the current user has admin role
     *
     * @return true if admin, false otherwise
     */
    public static boolean isAdmin() {
        if (!isAuthenticated()) {
            return false;
        }
        UserDTO user = getCurrentUser();
        return "ADMIN".equals(user.getRole());
    }
}
