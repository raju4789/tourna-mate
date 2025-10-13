package com.tournament.gateway.filters;

import com.tournament.gateway.dto.security.UserContext;
import com.tournament.gateway.exceptions.TokenValidationFailedException;
import com.tournament.gateway.security.JwtUtil;
import com.tournament.gateway.security.SecurityConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * Role-based authorization filter for API Gateway
 * This filter validates JWT tokens locally (no service call) and checks user roles
 */
@Component
public class RoleBasedAuthorizationFilter extends AbstractGatewayFilterFactory<RoleBasedAuthorizationFilter.Config> {

    private final RouteValidator routeValidator;
    private final JwtUtil jwtUtil;

    @Value("${jwt.tokenPrefix:Bearer }")
    private String tokenPrefix;

    @Value("${jwt.authorizationHeaderString:Authorization}")
    private String authorizationHeaderString;

    @Value("${jwt.headerSkipLength:7}")
    private int headerSkipLength;

    private static final Logger log = LoggerFactory.getLogger(RoleBasedAuthorizationFilter.class);

    @Autowired
    public RoleBasedAuthorizationFilter(RouteValidator routeValidator, JwtUtil jwtUtil) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // Skip authentication for public routes
            if (routeValidator.isSecured.test(request)) {
                
                // Extract token from Authorization header
                String token = extractToken(exchange);
                if (token == null) {
                    return onError(exchange, "Authorization header is missing or invalid", HttpStatus.UNAUTHORIZED);
                }

                try {
                    // Validate token expiration
                    if (jwtUtil.isTokenExpired(token)) {
                        return onError(exchange, "Token has expired", HttpStatus.UNAUTHORIZED);
                    }

                    // Extract user context from JWT
                    String username = jwtUtil.extractUsername(token);
                    List<String> roles = jwtUtil.extractRoles(token);
                    String email = jwtUtil.extractEmail(token);

                    UserContext userContext = UserContext.builder()
                            .username(username)
                            .roles(roles)
                            .email(email)
                            .build();

                    log.debug("User {} with roles {} accessing {}", username, roles, request.getPath());

                    // Validate required roles if configured
                    if (config.getRequiredRoles() != null && !config.getRequiredRoles().isEmpty()) {
                        boolean hasRequiredRole = userContext.hasAnyRole(
                                config.getRequiredRoles().toArray(new String[0])
                        );
                        
                        if (!hasRequiredRole) {
                            log.warn("User {} lacks required roles {} for {}", 
                                    username, config.getRequiredRoles(), request.getPath());
                            return onError(exchange, "Insufficient permissions", HttpStatus.FORBIDDEN);
                        }
                    }

                    // Propagate user context to downstream services via headers
                    ServerHttpRequest modifiedRequest = request.mutate()
                            .header(SecurityConstants.USER_HEADER, username)
                            .header(SecurityConstants.ROLES_HEADER, String.join(",", roles))
                            .header(SecurityConstants.EMAIL_HEADER, email != null ? email : "")
                            .build();

                    return chain.filter(exchange.mutate().request(modifiedRequest).build());

                } catch (Exception e) {
                    log.error("Token validation failed: {}", e.getMessage(), e);
                    return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
                }
            }

            return chain.filter(exchange);
        };
    }

    /**
     * Extract JWT token from Authorization header
     */
    private String extractToken(ServerWebExchange exchange) {
        List<String> authHeaders = exchange.getRequest().getHeaders().get(authorizationHeaderString);
        
        if (authHeaders == null || authHeaders.isEmpty()) {
            return null;
        }

        String authHeader = authHeaders.get(0);
        if (authHeader == null || !StringUtils.startsWithIgnoreCase(authHeader, tokenPrefix)) {
            return null;
        }

        return authHeader.substring(headerSkipLength);
    }

    /**
     * Return error response
     */
    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus status) {
        log.error("Authorization error: {} - {}", status, errorMessage);
        return Mono.error(new TokenValidationFailedException(errorMessage, status));
    }

    /**
     * Configuration class for specifying required roles per route
     */
    public static class Config {
        private List<String> requiredRoles;

        public List<String> getRequiredRoles() {
            return requiredRoles;
        }

        public void setRequiredRoles(List<String> requiredRoles) {
            this.requiredRoles = requiredRoles;
        }
        
        public void setRequiredRoles(String... roles) {
            this.requiredRoles = Arrays.asList(roles);
        }
    }
}

