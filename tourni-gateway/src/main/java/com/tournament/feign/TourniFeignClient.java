package com.tournament.feign;

import com.tournament.dto.common.CommonApiResponse;
import com.tournament.dto.security.*;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tourni-identity-service")
public interface TourniFeignClient {

    @PostMapping("/register")
    ResponseEntity<CommonApiResponse<AppAuthenticationResponse>> register(@Valid @RequestBody AppRegistrationRequest appUserRegistrationRequest);

    @PostMapping("/authenticate")
    ResponseEntity<CommonApiResponse<AppAuthenticationResponse>> authenticate(@Valid @RequestBody AppAuthenticationRequest appAuthenticationRequest);

    @GetMapping("/generateToken?username={username}")
    ResponseEntity<CommonApiResponse<AppAuthenticationResponse>> generateToken(@RequestParam String username);

    @PostMapping("/validateToken")
    ResponseEntity<CommonApiResponse<AppTokenValidationResponse>> validateToken(@Valid @RequestBody AppTokenValidationRequest appTokenValidationRequest);

}
