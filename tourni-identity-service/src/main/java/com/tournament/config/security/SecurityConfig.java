package com.tournament.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tournament.advice.GlobalExceptionHandler;
import com.tournament.dto.common.CommonApiResponse;
import com.tournament.exceptions.RecordNotFoundException;
import com.tournament.repository.auth.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final AppUserRepository appUserRepository;

    private final GlobalExceptionHandler globalExceptionHandler;

    private final ObjectMapper objectMapper;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> appUserRepository.findById(username)
                .orElseThrow(() -> new RecordNotFoundException("User not found with username: " + username));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint appAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            log.error("User not authenticated with error: " + authException.getMessage());
            ResponseEntity<CommonApiResponse<String>> errorResponse = globalExceptionHandler.handleUnAuthenticatedException(authException);
            response.setStatus(errorResponse.getStatusCode().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String jsonResponse = objectMapper.writeValueAsString(errorResponse.getBody());
            response.getWriter().write(jsonResponse);
        };
    }

    @Bean
    public AccessDeniedHandler appAccessDeniedHandler() {
        return (request, response, authException) -> {
            log.error("User not authorised with error: " + authException.getMessage());
            ResponseEntity<CommonApiResponse<String>> errorResponse = globalExceptionHandler.handleAccessDeniedException(authException);
            response.setStatus(errorResponse.getStatusCode().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String jsonResponse = objectMapper.writeValueAsString(errorResponse.getBody());
            response.getWriter().write(jsonResponse);
        };
    }

}
