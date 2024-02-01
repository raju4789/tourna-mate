package com.tournament.filters;

import com.tournament.exceptions.TokenValidationFailedException;
import com.tournament.service.TourniGatewayAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator routeValidator;
    private final TourniGatewayAuthenticationService tourniGatewayAuthenticationService;

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${jwt.authorizationHeaderString}")
    private String authorizationHeaderString;

    @Value("${jwt.headerSkipLength}")
    private int headerSkipLength;


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
