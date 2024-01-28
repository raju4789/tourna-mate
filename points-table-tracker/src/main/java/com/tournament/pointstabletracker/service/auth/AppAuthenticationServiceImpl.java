package com.tournament.pointstabletracker.service.auth;

import com.tournament.pointstabletracker.dto.auth.AppAuthenticationRequest;
import com.tournament.pointstabletracker.dto.auth.AppAuthenticationResponse;
import com.tournament.pointstabletracker.dto.auth.AppRegistrationRequest;
import com.tournament.pointstabletracker.entity.user.AppUser;
import com.tournament.pointstabletracker.exceptions.RecordAlreadyExistsException;
import com.tournament.pointstabletracker.exceptions.RecordNotFoundException;
import com.tournament.pointstabletracker.exceptions.UserUnAuthorizedException;
import com.tournament.pointstabletracker.mappers.PointsTableTrackerMappers;
import com.tournament.pointstabletracker.repository.user.AppUserRepository;
import com.tournament.pointstabletracker.service.security.JWTServiceImpl;
import com.tournament.pointstabletracker.utils.ApplicationConstants.AppUserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppAuthenticationServiceImpl implements AppAuthenticationService {

    private final AppUserRepository appUserRepository;
    private final JWTServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final PointsTableTrackerMappers pointsTableTrackerMappers;

    @Override
    public AppAuthenticationResponse authenticate(AppAuthenticationRequest appAuthenticationRequest) {
        String JWTToken;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(appAuthenticationRequest.getUsername(), appAuthenticationRequest.getPassword())
            );

            AppUser user = appUserRepository.findById(appAuthenticationRequest.getUsername())
                    .orElseThrow(() -> new RecordNotFoundException("User not found with username: " + appAuthenticationRequest.getUsername()));


            JWTToken = jwtService.generateToken(user);
        } catch (RecordNotFoundException e) {
            throw new RecordNotFoundException(e.getMessage());
        } catch (AuthenticationException e) {
            throw new UserUnAuthorizedException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return AppAuthenticationResponse.builder()
                .token(JWTToken)
                .build();
    }

    @Override
    public AppAuthenticationResponse register(AppRegistrationRequest appRegistrationRequest) {
        String JWTToken;
        try {
            appUserRepository.findById(appRegistrationRequest.getUsername())
                    .ifPresent(user -> {
                        throw new RecordAlreadyExistsException("User already exists with username: " + appRegistrationRequest.getUsername());
                    });
            AppUser user = pointsTableTrackerMappers.mapAppRegistrationRequestDTOToAppUser(appRegistrationRequest);

            appUserRepository.save(user);

            JWTToken = jwtService.generateToken(user);

            return AppAuthenticationResponse.builder()
                    .token(JWTToken)
                    .build();
        } catch (RecordAlreadyExistsException e) {
            throw new RecordAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


    }
}
