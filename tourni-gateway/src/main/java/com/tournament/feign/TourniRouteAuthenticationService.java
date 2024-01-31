package com.tournament.feign;

import com.tournament.dto.common.CommonApiResponse;
import com.tournament.dto.security.AppAuthenticationResponse;
import com.tournament.dto.security.AppRegistrationRequest;
import com.tournament.dto.security.AppTokenValidationResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tourni-identity-service")
public interface TourniRouteAuthenticationService {

    @PostMapping("/api/v1/auth/register")
    ResponseEntity<CommonApiResponse<AppAuthenticationResponse>> register(@Valid @RequestBody AppRegistrationRequest appUserRegistrationRequest);

    @GetMapping("/api/v1/auth/validateToken?token={token}")
    ResponseEntity<CommonApiResponse<AppTokenValidationResponse>> validateToken(@RequestParam String token);

    @GetMapping("/generateToken?username={username}")
    ResponseEntity<CommonApiResponse<AppAuthenticationResponse>> generateToken(@RequestParam String username);
}
