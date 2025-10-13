package com.tournament.service.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface JWTService {

    String extractUsername(String token);
    
    List<String> extractRoles(String token);
    
    Integer extractTokenVersion(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserDetails userDetails);
    
    String generateToken(UserDetails userDetails, Integer tokenVersion);

    String generateToken(
            Map<String, String> extraClaims,
            UserDetails userDetails
    );
    
    String generateToken(
            Map<String, String> extraClaims,
            UserDetails userDetails,
            Integer tokenVersion
    );

    boolean isTokenValid(String token, UserDetails userDetails);
    
    boolean isTokenValid(String token, UserDetails userDetails, Integer currentTokenVersion);
}
