package com.tournament.service.auth;

import com.tournament.dto.security.AppAuthenticationResponse;
import com.tournament.dto.security.AppRegistrationRequest;
import com.tournament.dto.security.AppTokenValidationResponse;

public interface AppAuthenticationService {

    AppAuthenticationResponse register(AppRegistrationRequest appRegistrationRequest);
    AppAuthenticationResponse generateToken(String username);
    AppTokenValidationResponse validateToken(String token);
}
