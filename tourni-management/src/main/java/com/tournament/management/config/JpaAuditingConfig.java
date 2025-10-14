package com.tournament.management.config;

import com.tournament.management.security.UserContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * JPA Auditing Configuration
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
     * 1. UserContextHolder (set by UserContextInterceptor from JWT)
     * 2. "system" (for system-initiated operations)
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            String username = UserContextHolder.getCurrentUsername();
            if (username != null && !username.isEmpty()) {
                return Optional.of(username);
            }
            // Fallback to system user for batch operations or non-authenticated requests
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

