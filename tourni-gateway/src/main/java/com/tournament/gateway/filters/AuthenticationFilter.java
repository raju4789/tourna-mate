package com.tournament.gateway.filters;

import com.tournament.gateway.service.TourniGatewayAuthenticationService;
import com.tournament.gateway.exceptions.TokenValidationFailedException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator routeValidator;
    private final TourniGatewayAuthenticationService tourniGatewayAuthenticationService;

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${jwt.authorizationHeaderString}")
    private String authorizationHeaderString;

    @Value("${jwt.headerSkipLength}")
    private int headerSkipLength;

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    public AuthenticationFilter(RouteValidator routeValidator, TourniGatewayAuthenticationService tourniGatewayAuthenticationService) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.tourniGatewayAuthenticationService = tourniGatewayAuthenticationService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                List<String> authorizationHeaders = exchange.getRequest().getHeaders().get(authorizationHeaderString);
                if (authorizationHeaders == null || authorizationHeaders.isEmpty()) {
                    return onError(exchange, "authorization header is missing in request", HttpStatus.BAD_REQUEST);
                }

                final String authorizationHeader = authorizationHeaders.get(0);

                if (authorizationHeader == null || !StringUtils.startsWithIgnoreCase(authorizationHeader, tokenPrefix)) {
                    return onError(exchange, "bearer is missing or invalid", HttpStatus.BAD_REQUEST);
                }

                final String token = authorizationHeader.substring(headerSkipLength);

                return tourniGatewayAuthenticationService.validateToken(token)
                        .flatMap(response -> chain.filter(exchange))
                        .onErrorResume(e -> {
                            log.error("Error while validating token with error : {}", e.getMessage(), e);
                            return onError(exchange, "Error while validating token with error " +
                                    (e.getMessage() != null ? e.getMessage() : "unknown"), HttpStatus.INTERNAL_SERVER_ERROR);
                        });
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus status) {
        return Mono.error(new TokenValidationFailedException(errorMessage, status));
    }

    public static class Config {
    }
}
