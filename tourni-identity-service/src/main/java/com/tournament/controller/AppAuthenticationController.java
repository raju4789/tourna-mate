package com.tournament.controller;

import com.tournament.dto.common.CommonApiResponse;
import com.tournament.dto.security.AppAuthenticationResponse;
import com.tournament.dto.security.AppRegistrationRequest;
import com.tournament.dto.security.AppTokenValidationResponse;
import com.tournament.service.auth.AppAuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication controller")
public class AppAuthenticationController {

    private final AppAuthenticationService appAuthenticationService;

    @PostMapping("/register")
    public ResponseEntity<CommonApiResponse<AppAuthenticationResponse>> register(@Valid @RequestBody AppRegistrationRequest appUserRegistrationRequest) {

        AppAuthenticationResponse appAuthenticationResponse = appAuthenticationService.register(appUserRegistrationRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonApiResponse<>(appAuthenticationResponse));
    }

    @GetMapping("/generateToken?username={username}")
    public ResponseEntity<CommonApiResponse<AppAuthenticationResponse>> generateToken(@RequestParam String username) {

        AppAuthenticationResponse appAuthenticationResponse = appAuthenticationService.generateToken(username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonApiResponse<>(appAuthenticationResponse));
    }

    @GetMapping("/validateToken?token={token}")
    public ResponseEntity<CommonApiResponse<AppTokenValidationResponse>> validateToken(@RequestParam String token) {

        AppTokenValidationResponse appTokenValidationResponse = appAuthenticationService.validateToken(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonApiResponse<>(appTokenValidationResponse));
    }
}
