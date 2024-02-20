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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tournament.dto.security.AppAuthenticationResponse;
import com.tournament.dto.security.AppRegistrationRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppAuthenticationServiceImpl implements AppAuthenticationService {

    private final AppUserRepository appUserRepository;
    private final JWTServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AppTokenValidationResponse validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token);

            AppUser user = appUserRepository.findById(username)
                    .orElseThrow(() -> new RecordNotFoundException("User not found with username: " + username));


            boolean isValid = jwtService.isTokenValid(token, user);
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

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(appAuthenticationRequest.getUsername(), appAuthenticationRequest.getPassword())
            );

            AppUser user = appUserRepository.findById(appAuthenticationRequest.getUsername())
                    .orElseThrow(() -> new RecordNotFoundException("User not found with username: " + appAuthenticationRequest.getUsername()));


            String JWTToken = jwtService.generateToken(user);

            return AppAuthenticationResponse.builder()
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .username(user.getUsername())
                    .role(user.getRole().toString())
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
            appUserRepository.findById(appRegistrationRequest.getUsername())
                    .ifPresent(user -> {
                        throw new RecordAlreadyExistsException("User already exists with username: " + appRegistrationRequest.getUsername());
                    });
            AppUser user = AppUser.builder()
                    .email(appRegistrationRequest.getEmail())
                    .firstName(appRegistrationRequest.getFirstName())
                    .lastName(appRegistrationRequest.getLastName())
                    .username(appRegistrationRequest.getUsername())
                    .password(passwordEncoder.encode(appRegistrationRequest.getPassword()))
                    .role(AppUserRole.USER)
                    .isActive(true)
                    .recordCreatedBy(UUID.randomUUID().toString())
                    .recordCreatedDate(LocalDateTime.now())
                    .build();

            appUserRepository.save(user);

            String JWTToken = jwtService.generateToken(user);

            return AppAuthenticationResponse.builder()
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .username(user.getUsername())
                    .role(user.getRole().toString())
                    .token(JWTToken)
                    .build();
        } catch (RecordAlreadyExistsException e) {
            log.error(e.getMessage());
            throw new RecordAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            log.error("unknown exception occurred registering user with error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public AppAuthenticationResponse generateToken(String username) {

        try {
            AppUser user = appUserRepository.findById(username)
                    .orElseThrow(() -> new RecordNotFoundException("User not found with username: " + username));
            String token = jwtService.generateToken(user);

            return AppAuthenticationResponse.builder()
                    .token(token)
                    .build();
        } catch (RecordNotFoundException e) {
            log.error(e.getMessage());
            throw new RecordNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("unknown exception occurred generating token with error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

}
