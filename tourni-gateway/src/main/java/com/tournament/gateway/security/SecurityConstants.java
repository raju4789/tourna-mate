package com.tournament.gateway.security;

/**
 * Security constants for authorization and authentication
 */
public class SecurityConstants {
    
    // HTTP Headers for user context propagation
    public static final String USER_HEADER = "X-User-Username";
    public static final String ROLES_HEADER = "X-User-Roles";
    public static final String EMAIL_HEADER = "X-User-Email";
    
    // Role definitions
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    
    // Role prefixes for Spring Security
    public static final String ROLE_PREFIX = "ROLE_";
    
    private SecurityConstants() {
        // Utility class
    }
}

