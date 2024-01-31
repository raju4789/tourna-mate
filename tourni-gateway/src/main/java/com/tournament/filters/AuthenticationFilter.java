package com.tournament.filters;

import com.tournament.dto.common.CommonApiResponse;
import com.tournament.dto.security.AppTokenValidationResponse;
import com.tournament.feign.TourniRouteAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator routeValidator;

    @Lazy
    private final TourniRouteAuthenticationService routeAuthenticationService;

    public AuthenticationFilter(RouteValidator routeValidator, TourniRouteAuthenticationService routeAuthenticationService) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.routeAuthenticationService = routeAuthenticationService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey("Authorization")) {
                    throw new RuntimeException("Authorization header is missing in request");
                }

                final String authorizationHeader = exchange.getRequest().getHeaders().get("Authorization").get(0);

                if(authorizationHeader != null && !authorizationHeader.startsWith("Bearer ")) {
                    throw new RuntimeException("Authorization header is invalid");
                }

                final String token = authorizationHeader.substring(7);

                try {

                    ResponseEntity<CommonApiResponse<AppTokenValidationResponse>>  res = routeAuthenticationService.validateToken(token);
                    if(!res.getBody().isSuccess()) {
                        throw new RuntimeException("Authorization header is invalid");
                    }
                }catch (Exception e) {
                    throw new RuntimeException("Authorization header is invalid");
                }
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {

    }
}
