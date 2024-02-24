package com.tournament.gateway.service;

import com.tournament.gateway.dto.common.CommonApiResponse;
import com.tournament.gateway.dto.security.AppTokenValidationRequest;
import com.tournament.gateway.dto.security.AppTokenValidationResponse;
import com.tournament.gateway.exceptions.TokenValidationFailedException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TourniGatewayAuthenticationServiceImpl implements TourniGatewayAuthenticationService {

    private final WebClient.Builder webClientBuilder;

    private WebClient webClient;
    @Value("${identity.service.baseUrl:}")
    private String identityServiceBaseUrl;

    private static final Logger log = LoggerFactory.getLogger(TourniGatewayAuthenticationServiceImpl.class);

    public TourniGatewayAuthenticationServiceImpl(@LoadBalanced WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder.baseUrl(identityServiceBaseUrl).build();
    }

    public Mono<AppTokenValidationResponse> validateToken(String token) {
        AppTokenValidationRequest request = AppTokenValidationRequest.builder().token(token).build();

        return webClient.post()
                .uri("/api/v1/auth/validateToken")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CommonApiResponse<AppTokenValidationResponse>>() {
                })
                .flatMap(response -> {
                    if (response == null || !response.isSuccess() || response.getData() == null || !response.getData().isValid()) {
                        log.error("Token validation failed");
                        return Mono.error(new TokenValidationFailedException("Token validation failed", HttpStatus.UNAUTHORIZED));
                    }
                    log.info("Token validation successful");
                    return Mono.just(response.getData());
                });
    }
}