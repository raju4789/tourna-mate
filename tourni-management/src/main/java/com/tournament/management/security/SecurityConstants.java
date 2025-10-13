package com.tournament.management.security;

/**
 * Security constants for authorization
 * Must match gateway constants for user context propagation
 */
public class SecurityConstants {
    
    // HTTP Headers for user context (set by gateway)
    public static final String USER_HEADER = "X-User-Username";
    public static final String ROLES_HEADER = "X-User-Roles";
    public static final String EMAIL_HEADER = "X-User-Email";
    
    // Role definitions
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    
    private SecurityConstants() {
        // Utility class
    }
}

