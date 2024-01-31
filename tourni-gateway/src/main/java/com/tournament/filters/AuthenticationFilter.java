package com.tournament.filters;

import com.tournament.dto.common.CommonApiResponse;
import com.tournament.dto.security.AppTokenValidationRequest;
import com.tournament.dto.security.AppTokenValidationResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

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
                    throw new RuntimeException("Authorization header is missing in request");
                }

                final String authorizationHeader = authorizationHeaders.get(0);

                if (authorizationHeader == null || !StringUtils.startsWithIgnoreCase(authorizationHeader, "Bearer ")) {
                    throw new RuntimeException("Authorization header is missing or invalid");
                }

                final String token = authorizationHeader.substring(7);

                try {
                    AppTokenValidationRequest req = AppTokenValidationRequest.builder().token(token).build();

                    CommonApiResponse body = webClient.post()
                            .uri("/api/v1/auth/validateToken")
                            .bodyValue(BodyInserters.fromValue(req))
                            .retrieve()
                            .bodyToMono(CommonApiResponse.class)
                            .block();


                    if (body == null || !body.isSuccess() || body.getData() == null || !body.getData().isValid()) {
                        throw new RuntimeException("Authorization header is invalid: " +
                                (body != null ? body.getErrorDetails().getErrorMessage() : "Unknown error"));
                    }

                } catch (Exception e) {
                    log.error("Exception while processing authentication: {}", e.getMessage(), e);
                    throw new RuntimeException("Unknown Authorization header is invalid" +
                            (e.getMessage() != null ? e.getMessage() : ""));
                }
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}
