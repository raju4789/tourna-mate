package com.tournament.pointstabletracker.service.auth;

import com.tournament.pointstabletracker.dto.auth.AppAuthenticationRequest;
import com.tournament.pointstabletracker.dto.auth.AppAuthenticationResponse;
import com.tournament.pointstabletracker.dto.auth.AppRegistrationRequest;

public interface AppAuthenticationService {

    AppAuthenticationResponse authenticate(AppAuthenticationRequest appAuthenticationRequest);

    AppAuthenticationResponse register(AppRegistrationRequest appRegistrationRequest);

}
