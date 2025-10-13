package com.tournament.management.security;

/**
 * Thread-local holder for user context
 * Stores user information extracted from request headers
 */
public class UserContextHolder {
    
    private static final ThreadLocal<UserContext> contextHolder = new ThreadLocal<>();
    
    private UserContextHolder() {
        // Utility class - private constructor
    }
    
    public static void setContext(UserContext context) {
        contextHolder.set(context);
    }
    
    public static UserContext getContext() {
        UserContext context = contextHolder.get();
        if (context == null) {
            // Return empty context if not set
            context = UserContext.builder().build();
        }
        return context;
    }
    
    public static void clear() {
        contextHolder.remove();
    }
    
    /**
     * Get current username
     */
    public static String getCurrentUsername() {
        UserContext context = getContext();
        return context != null ? context.getUsername() : null;
    }
    
    /**
     * Check if current user is admin
     */
    public static boolean isCurrentUserAdmin() {
        UserContext context = getContext();
        return context != null && context.isAdmin();
    }
}

