package com.tournament.management.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

/**
 * Interceptor to extract user context from HTTP headers and populate ThreadLocal
 */
@Slf4j
@Component
public class UserContextInterceptor implements HandlerInterceptor {
    
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String username = request.getHeader(SecurityConstants.USER_HEADER);
        String rolesStr = request.getHeader(SecurityConstants.ROLES_HEADER);
        String email = request.getHeader(SecurityConstants.EMAIL_HEADER);
        
        if (username != null) {
            List<String> roles = rolesStr != null && !rolesStr.isEmpty()
                    ? Arrays.asList(rolesStr.split(","))
                    : List.of();
            
            UserContext userContext = UserContext.builder()
                    .username(username)
                    .roles(roles)
                    .email(email)
                    .build();
            
            UserContextHolder.setContext(userContext);
            log.debug("User context set: {} with roles {}", username, roles);
        }
        
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                                Object handler, Exception ex) {
        // Clean up thread local to prevent memory leaks
        UserContextHolder.clear();
    }
}

