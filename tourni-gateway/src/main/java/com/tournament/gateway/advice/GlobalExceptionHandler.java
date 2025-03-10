package com.tournament.gateway.advice;

import com.tournament.gateway.dto.common.CommonApiResponse;
import com.tournament.gateway.dto.common.ErrorDetails;
import com.tournament.gateway.exceptions.TokenValidationFailedException;
import com.tournament.gateway.service.TourniGatewayAuthenticationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(TokenValidationFailedException.class)
    public ResponseEntity<CommonApiResponse<String>> handleTokenValidationFailedException(TokenValidationFailedException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder().errorCode(ex.getStatus().value()).errorMessage(ex.getMessage()).build();
        CommonApiResponse<String> commonApiResponse = CommonApiResponse.<String>builder().errorDetails(errorDetails).build();
        return ResponseEntity.status(ex.getStatus()).body(commonApiResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        log.error("RuntimeException occurred: ", ex);
        ErrorDetails errorDetails = ErrorDetails.builder().errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).errorMessage(ex.getMessage()).build();
        CommonApiResponse<String> commonApiResponse = CommonApiResponse.<String>builder().errorDetails(errorDetails).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonApiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonApiResponse<String>> handleDefaultException(Exception ex) {
        log.error("Exception occurred: ", ex);
        ErrorDetails errorDetails = ErrorDetails.builder().errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).errorMessage(ex.getMessage()).build();
        CommonApiResponse<String> commonApiResponse = CommonApiResponse.<String>builder().errorDetails(errorDetails).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonApiResponse);
    }
}
