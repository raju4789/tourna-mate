package com.tournament.gateway.dto.security;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppTokenValidationResponse {
    private boolean isValid;
}
