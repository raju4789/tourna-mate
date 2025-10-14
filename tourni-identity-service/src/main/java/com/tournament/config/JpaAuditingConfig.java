package com.tournament.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * JPA Auditing Configuration for Identity Service
 * 
 * Enables automatic population of:
 * - @CreatedBy and @LastModifiedBy with current username
 * - @CreatedDate and @LastModifiedDate with current timestamp
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider", dateTimeProviderRef = "auditingDateTimeProvider")
public class JpaAuditingConfig {

    /**
     * Provides current auditor (username) for @CreatedBy and @LastModifiedBy
     * 
     * Resolution order:
     * 1. Spring Security Context (for authenticated users)
     * 2. "system" (for system-initiated operations, e.g., user registration)
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && 
                authentication.isAuthenticated() && 
                !"anonymousUser".equals(authentication.getPrincipal())) {
                
                String username = authentication.getName();
                if (username != null && !username.isEmpty()) {
                    return Optional.of(username);
                }
            }
            
            // Fallback to system user for registration and batch operations
            return Optional.of("system");
        };
    }

    /**
     * Provides current timestamp for @CreatedDate and @LastModifiedDate
     */
    @Bean
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now());
    }
}

