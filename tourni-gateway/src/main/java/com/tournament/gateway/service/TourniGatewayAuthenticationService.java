package com.tournament.gateway.service;

import com.tournament.gateway.dto.security.AppTokenValidationResponse;
import reactor.core.publisher.Mono;

public interface TourniGatewayAuthenticationService {

    Mono<AppTokenValidationResponse> validateToken(String token);
}
