package com.tournament.dto.security;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppAuthenticationResponse {
   String username;
   String token;
   String role;
}
