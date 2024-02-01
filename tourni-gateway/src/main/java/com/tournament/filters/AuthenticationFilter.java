package com.tournament.filters;

import com.tournament.dto.common.CommonApiResponse;
import com.tournament.dto.security.AppTokenValidationRequest;
import com.tournament.dto.security.AppTokenValidationResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator routeValidator;
    private final WebClient webClient;

    @Autowired
    public AuthenticationFilter(RouteValidator routeValidator, WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.webClient = webClientBuilder.baseUrl("http://tourni-identity-service").build();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                List<String> authorizationHeaders = exchange.getRequest().getHeaders().get("Authorization");
                if (authorizationHeaders == null || authorizationHeaders.isEmpty()) {
                    return onError(exchange, "Authorization header is missing in request");
                }

                final String authorizationHeader = authorizationHeaders.get(0);

                if (authorizationHeader == null || !StringUtils.startsWithIgnoreCase(authorizationHeader, "Bearer ")) {
                    return onError(exchange, "Authorization header is missing or invalid");
                }

                final String token = authorizationHeader.substring(7);

                AppTokenValidationRequest req = AppTokenValidationRequest.builder().token(token).build();

                return webClient.post()
                        .uri("/api/v1/auth/validateToken")
                        .bodyValue(BodyInserters.fromValue(req))
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<CommonApiResponse<AppTokenValidationResponse>>() {
                        })
                        .flatMap(body -> {
                            if (body == null || !body.isSuccess() || body.getData() == null || !body.getData().isValid()) {
                                return onError(exchange, "Authorization header is invalid: " +
                                        (body != null ? body.getErrorDetails().getErrorMessage() : "Unknown error"));
                            }
                            return chain.filter(exchange);
                        })
                        .onErrorResume(e -> onError(exchange, "Unknown Authorization header is invalid " +
                                (e.getMessage() != null ? e.getMessage() : "")));
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage) {
        log.error("Exception while processing authentication: {}", errorMessage);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
    }
}
