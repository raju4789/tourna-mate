package com.tournament.filters;

import com.tournament.dto.common.CommonApiResponse;
import com.tournament.dto.security.AppTokenValidationRequest;
import com.tournament.dto.security.AppTokenValidationResponse;
import com.tournament.feign.TourniFeignClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator routeValidator;

    private final TourniFeignClient tourniFeignClient;

    public AuthenticationFilter(RouteValidator routeValidator, @Lazy TourniFeignClient tourniFeignClient) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.tourniFeignClient = tourniFeignClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey("Authorization")) {
                    throw new RuntimeException("Authorization header is missing in request");
                }

                final String authorizationHeader = exchange.getRequest().getHeaders().get("Authorization").get(0);

                if (authorizationHeader != null && !authorizationHeader.startsWith("Bearer ")) {
                    throw new RuntimeException("Authorization header is invalid");
                }

                final String token = authorizationHeader.substring(7);

                try {
                    AppTokenValidationRequest req = AppTokenValidationRequest.builder().token(token).build();
                    ResponseEntity<CommonApiResponse<AppTokenValidationResponse>> res = tourniFeignClient.validateToken(req);

                    if (!res.getBody().isSuccess() || !res.getBody().getData().isValid()) {
                        throw new RuntimeException("Authorization header is invalid");
                    }

                } catch (Exception e) {
                    throw new RuntimeException("Authorization header is invalid");
                }
            }
            return chain.filter(exchange);
        };
    }

    public static class Config { }
}
