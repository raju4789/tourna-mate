package com.tournament.pointstabletracker.controller;

import com.tournament.pointstabletracker.dto.CommonApiResponse;
import com.tournament.pointstabletracker.dto.auth.AppAuthenticationRequest;
import com.tournament.pointstabletracker.dto.auth.AppAuthenticationResponse;
import com.tournament.pointstabletracker.dto.auth.AppRegistrationRequest;
import com.tournament.pointstabletracker.service.auth.AppAuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/authenticate")
    public ResponseEntity<CommonApiResponse<AppAuthenticationResponse>> authenticate(@Valid @RequestBody AppAuthenticationRequest appAuthenticationRequest) {

        AppAuthenticationResponse appAuthenticationResponse = appAuthenticationService.authenticate(appAuthenticationRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonApiResponse<>(appAuthenticationResponse));
    }
}
