package com.tournament.dto.security;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppTokenValidationResponse {
    private boolean isValid;
}
