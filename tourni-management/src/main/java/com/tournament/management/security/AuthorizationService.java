package com.tournament.management.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for authorization checks
 * Used by custom annotations and SpEL expressions
 */
@Service
public class AuthorizationService {
    
    private static final Logger log = LoggerFactory.getLogger(AuthorizationService.class);
    
    /**
     * Check if current user is authenticated
     */
    public boolean isAuthenticated() {
        UserContext context = UserContextHolder.getContext();
        boolean authenticated = context != null && context.getUsername() != null;
        
        if (!authenticated) {
            log.warn("Unauthorized access attempt - no user context");
        }
        
        return authenticated;
    }
    
    /**
     * Check if current user is admin
     * Uses role hierarchy - ADMIN inherits all permissions
     */
    public boolean isAdmin() {
        UserContext context = UserContextHolder.getContext();
        boolean isAdmin = context != null && context.isAdmin();
        
        if (!isAdmin) {
            log.warn("Forbidden access attempt - user {} is not admin", 
                    context != null ? context.getUsername() : "unknown");
        }
        
        return isAdmin;
    }
    
    /**
     * Check if current user has a specific role
     * Implements role hierarchy: ADMIN inherits all roles
     */
    public boolean hasRole(String role) {
        UserContext context = UserContextHolder.getContext();
        if (context == null) {
            return false;
        }
        
        // Role hierarchy handled in UserContext.hasRole()
        return context.hasRole(role);
    }
    
    /**
     * Check if current user has any of the specified roles
     * Implements role hierarchy: ADMIN satisfies any role requirement
     */
    public boolean hasAnyRole(String... roles) {
        UserContext context = UserContextHolder.getContext();
        if (context == null) {
            return false;
        }
        
        // Role hierarchy handled in UserContext.hasAnyRole()
        return context.hasAnyRole(roles);
    }
    
    /**
     * Check if current user owns a resource (username matches)
     */
    public boolean isOwner(String username) {
        UserContext context = UserContextHolder.getContext();
        boolean isOwner = context != null && context.getUsername().equals(username);
        
        if (!isOwner) {
            log.warn("Forbidden access attempt - user {} tried to access resource owned by {}", 
                    context != null ? context.getUsername() : "unknown", username);
        }
        
        return isOwner;
    }
    
    /**
     * Check if current user is admin or owner
     */
    public boolean isAdminOrOwner(String username) {
        return isAdmin() || isOwner(username);
    }
}

