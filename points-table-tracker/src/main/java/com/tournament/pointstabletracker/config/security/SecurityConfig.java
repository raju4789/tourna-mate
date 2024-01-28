package com.tournament.pointstabletracker.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tournament.pointstabletracker.advice.GlobalExceptionHandler;
import com.tournament.pointstabletracker.dto.CommonApiResponse;
import com.tournament.pointstabletracker.exceptions.RecordNotFoundException;
import com.tournament.pointstabletracker.repository.user.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SecurityConfig {

    private final AppUserRepository appUserRepository;

    private final GlobalExceptionHandler globalExceptionHandler;

    private final ObjectMapper objectMapper;
    private static final Logger logger = (Logger) LoggerFactory.getLogger(SecurityConfig.class);


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
            logger.error("User not authenticated with error: " + authException.getMessage());
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
            logger.error("User not authorised with error: " + authException.getMessage());
            ResponseEntity<CommonApiResponse<String>> errorResponse = globalExceptionHandler.handleAccessDeniedException(authException);
            response.setStatus(errorResponse.getStatusCode().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String jsonResponse = objectMapper.writeValueAsString(errorResponse.getBody());
            response.getWriter().write(jsonResponse);
        };
    }

}
