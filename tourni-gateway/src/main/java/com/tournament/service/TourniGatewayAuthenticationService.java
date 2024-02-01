package com.tournament.service;

import com.tournament.dto.security.AppTokenValidationResponse;
import reactor.core.publisher.Mono;

public interface TourniGatewayAuthenticationService {

    Mono<AppTokenValidationResponse> validateToken(String token);
}
