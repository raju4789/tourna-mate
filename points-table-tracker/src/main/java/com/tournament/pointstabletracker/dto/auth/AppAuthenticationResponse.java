package com.tournament.pointstabletracker.dto.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppAuthenticationResponse {
    private String token;
}
