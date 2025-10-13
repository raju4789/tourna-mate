package com.tournament.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JWTServiceImpl implements JWTService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expirationTime}")
    private int expirationTime;

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    @Override
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        String rolesStr = claims.get("roles", String.class);
        if (rolesStr == null || rolesStr.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(rolesStr.split(","))
                .map(String::trim)
                .toList();
    }
    
    @Override
    public Integer extractTokenVersion(String token) {
        Claims claims = extractAllClaims(token);
        Object version = claims.get("tokenVersion");
        if (version == null) {
            return 0;  // Default for old tokens without version
        }
        return version instanceof Integer ? (Integer) version : Integer.parseInt(version.toString());
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, 0);
    }
    
    @Override
    public String generateToken(UserDetails userDetails, Integer tokenVersion) {
        return generateToken(new HashMap<>(), userDetails, tokenVersion);
    }

    @Override
    public String generateToken(
            Map<String, String> extraClaims,
            UserDetails userDetails
    ) {
        return generateToken(extraClaims, userDetails, 0);
    }
    
    @Override
    public String generateToken(
            Map<String, String> extraClaims,
            UserDetails userDetails,
            Integer tokenVersion
    ) {
        // Extract roles/authorities from UserDetails
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)  // Add roles as claim
                .claim("authorities", roles)  // Alternative claim name for compatibility
                .claim("tokenVersion", tokenVersion)  // Add token version for invalidation
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails, Integer currentTokenVersion) {
        final String username = extractUsername(token);
        final Integer tokenVersion = extractTokenVersion(token);
        
        // Check username, expiration, and token version
        boolean isValid = username.equals(userDetails.getUsername()) 
                && !isTokenExpired(token)
                && tokenVersion.equals(currentTokenVersion);
        
        if (!isValid && !tokenVersion.equals(currentTokenVersion)) {
            // Token version mismatch - roles have changed
            return false;
        }
        
        return isValid;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
