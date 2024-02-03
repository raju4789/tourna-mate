package com.tournament.gateway.dto.security;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppTokenValidationRequest {

    @NotBlank(message = "Token is mandatory")
    private String token;
}