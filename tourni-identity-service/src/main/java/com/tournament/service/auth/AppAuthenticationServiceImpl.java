package com.tournament.service.auth;

import com.tournament.dto.security.AppAuthenticationRequest;
import com.tournament.dto.security.AppTokenValidationResponse;
import com.tournament.entity.AppUser;
import com.tournament.exceptions.RecordAlreadyExistsException;
import com.tournament.exceptions.RecordNotFoundException;
import com.tournament.exceptions.UserUnAuthorizedException;
import com.tournament.repository.auth.AppUserRepository;
import com.tournament.service.security.JWTServiceImpl;
import com.tournament.utils.ApplicationConstants.AppUserRole;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tournament.dto.security.AppAuthenticationResponse;
import com.tournament.dto.security.AppRegistrationRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppAuthenticationServiceImpl implements AppAuthenticationService {

    private final AppUserRepository appUserRepository;
    private final JWTServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private static final Logger log = LoggerFactory.getLogger(AppAuthenticationServiceImpl.class);

    @Override
    public AppTokenValidationResponse validateToken(String token) {
        try {
            log.info("Validating token");
            String username = jwtService.extractUsername(token);

            AppUser user = appUserRepository.findById(username)
                    .orElseThrow(() -> new RecordNotFoundException("User not found with username: " + username));

            // Validate token with version check
            boolean isValid = jwtService.isTokenValid(token, user, user.getTokenVersion());
            
            if (!isValid) {
                log.warn("Token validation failed for user {}", username);
            }
            
            return AppTokenValidationResponse.builder()
                    .isValid(isValid)
                    .build();
        } catch (RecordNotFoundException e) {
            log.error(e.getMessage());
            throw new RecordNotFoundException(e.getMessage());
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            throw new UserUnAuthorizedException(e.getMessage());
        } catch (Exception e) {
            log.error("unknown exception occurred validating token with error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public AppAuthenticationResponse authenticate(AppAuthenticationRequest appAuthenticationRequest) {
        try {
            log.info("Authenticating user");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(appAuthenticationRequest.getUsername(), appAuthenticationRequest.getPassword())
            );

            AppUser user = appUserRepository.findById(appAuthenticationRequest.getUsername())
                    .orElseThrow(() -> new RecordNotFoundException("User not found with username: " + appAuthenticationRequest.getUsername()));

            // Generate token with current token version
            String JWTToken = jwtService.generateToken(user, user.getTokenVersion());
            
            // Get roles as comma-separated string for response
            String rolesStr = user.getRoles().stream()
                    .map(AppUserRole::toString)
                    .reduce((a, b) -> a + "," + b)
                    .orElse("USER");

            return AppAuthenticationResponse.builder()
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .username(user.getUsername())
                    .role(rolesStr)  // Return all roles as comma-separated string
                    .token(JWTToken)
                    .build();
        } catch (RecordNotFoundException e) {
            log.error(e.getMessage());
            throw new RecordNotFoundException(e.getMessage());
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            throw new UserUnAuthorizedException(e.getMessage());
        } catch (Exception e) {
            log.error("unknown exception occurred authenticating user with error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public AppAuthenticationResponse register(AppRegistrationRequest appRegistrationRequest) {
        try {
            log.info("Registering user");
            appUserRepository.findById(appRegistrationRequest.getUsername())
                    .ifPresent(user -> {
                        throw new RecordAlreadyExistsException("User already exists with username: " + appRegistrationRequest.getUsername());
                    });
            // Build user with USER role by default
            AppUser user = AppUser.builder()
                    .email(appRegistrationRequest.getEmail())
                    .firstName(appRegistrationRequest.getFirstName())
                    .lastName(appRegistrationRequest.getLastName())
                    .username(appRegistrationRequest.getUsername())
                    .password(passwordEncoder.encode(appRegistrationRequest.getPassword()))
                    .roles(new java.util.HashSet<>())  // Initialize roles set
                    .tokenVersion(0)  // Initial token version
                    .isActive(true)
                    .recordCreatedBy(UUID.randomUUID().toString())
                    .recordCreatedDate(LocalDateTime.now())
                    .build();
            
            // Add USER role
            user.getRoles().add(AppUserRole.USER);

            appUserRepository.save(user);

            // Generate token with version
            String JWTToken = jwtService.generateToken(user, user.getTokenVersion());
            
            String rolesStr = user.getRoles().stream()
                    .map(AppUserRole::toString)
                    .reduce((a, b) -> a + "," + b)
                    .orElse("USER");

            return AppAuthenticationResponse.builder()
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .username(user.getUsername())
                    .role(rolesStr)
                    .token(JWTToken)
                    .build();
        } catch (RecordAlreadyExistsException e) {
            log.error(e.getMessage());
            throw new RecordAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            log.error("unknown exception occurred registering user", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public AppAuthenticationResponse generateToken(String username) {

        try {
            AppUser user = appUserRepository.findById(username)
                    .orElseThrow(() -> new RecordNotFoundException("User not found with username: " + username));
            
            // Generate token with current version
            String token = jwtService.generateToken(user, user.getTokenVersion());

            return AppAuthenticationResponse.builder()
                    .token(token)
                    .build();
        } catch (RecordNotFoundException e) {
            log.error(e.getMessage());
            throw new RecordNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("unknown exception occurred generating token", e);
            throw new RuntimeException(e.getMessage());
        }

    }

}
