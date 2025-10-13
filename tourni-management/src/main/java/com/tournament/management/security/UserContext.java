package com.tournament.management.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * User context extracted from HTTP headers
 * This is populated by the API Gateway from JWT token
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {
    
    private String username;
    private List<String> roles;
    private String email;
    
    /**
     * Check if user has a specific role
     * Implements role hierarchy: ADMIN inherits all USER permissions
     */
    public boolean hasRole(String role) {
        if (roles == null) {
            return false;
        }
        
        // ADMIN has all permissions - inherits USER role automatically
        if (roles.contains(SecurityConstants.ROLE_ADMIN)) {
            return true;
        }
        
        return roles.contains(role);
    }
    
    /**
     * Check if user has any of the specified roles
     * Implements role hierarchy: ADMIN satisfies any role requirement
     */
    public boolean hasAnyRole(String... roles) {
        if (this.roles == null || roles == null) {
            return false;
        }
        
        // ADMIN has all permissions
        if (this.roles.contains(SecurityConstants.ROLE_ADMIN)) {
            return true;
        }
        
        return Arrays.stream(roles)
                .anyMatch(this.roles::contains);
    }
    
    /**
     * Check if user is admin
     */
    public boolean isAdmin() {
        return roles != null && roles.contains(SecurityConstants.ROLE_ADMIN);
    }
    
    /**
     * Check if user is regular user (has USER role)
     * Note: ADMIN also returns true due to role hierarchy
     */
    public boolean isUser() {
        return hasRole(SecurityConstants.ROLE_USER);
    }
}

