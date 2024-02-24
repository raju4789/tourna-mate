package com.tournament.advice;


import com.tournament.dto.common.CommonApiResponse;
import com.tournament.dto.common.ErrorDetails;
import com.tournament.exceptions.InvalidRequestException;
import com.tournament.exceptions.RecordAlreadyExistsException;
import com.tournament.exceptions.RecordNotFoundException;
import com.tournament.exceptions.UserUnAuthorizedException;
import com.tournament.service.auth.AppAuthenticationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AppAuthenticationServiceImpl.class);

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<CommonApiResponse<String>> handleInvalidRequestException(InvalidRequestException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder().errorCode(HttpStatus.BAD_REQUEST.value()).errorMessage(ex.getMessage()).build();
        CommonApiResponse<String> commonApiResponse = CommonApiResponse.<String>builder().errorDetails(errorDetails).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonApiResponse);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<CommonApiResponse<String>> handleRecordNotFoundException(RecordNotFoundException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder().errorCode(HttpStatus.BAD_REQUEST.value()).errorMessage(ex.getMessage()).build();
        CommonApiResponse<String> commonApiResponse = CommonApiResponse.<String>builder().errorDetails(errorDetails).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonApiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonApiResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> fieldErrorMessages = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();

        ErrorDetails errorDetails = ErrorDetails.builder().errorCode(HttpStatus.BAD_REQUEST.value()).errorMessage(fieldErrorMessages.toString()).build();

        CommonApiResponse<String> commonApiResponse = CommonApiResponse.<String>builder().errorDetails(errorDetails).build();

        return ResponseEntity.badRequest().body(commonApiResponse);
    }

    @ExceptionHandler(UserUnAuthorizedException.class)
    public ResponseEntity<CommonApiResponse<String>> handleUnAuthorisedException(UserUnAuthorizedException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder().errorCode(HttpStatus.FORBIDDEN.value()).errorMessage(ex.getMessage()).build();
        CommonApiResponse<String> commonApiResponse = CommonApiResponse.<String>builder().errorDetails(errorDetails).build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(commonApiResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonApiResponse<String>> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder().errorCode(HttpStatus.FORBIDDEN.value()).errorMessage(ex.getMessage()).build();
        CommonApiResponse<String> commonApiResponse = CommonApiResponse.<String>builder().errorDetails(errorDetails).build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(commonApiResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CommonApiResponse<String>> handleUnAuthenticatedException(AuthenticationException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder().errorCode(HttpStatus.UNAUTHORIZED.value()).errorMessage(ex.getMessage()).build();
        CommonApiResponse<String> commonApiResponse = CommonApiResponse.<String>builder().errorDetails(errorDetails).build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(commonApiResponse);
    }

    @ExceptionHandler(RecordAlreadyExistsException.class)
    public ResponseEntity<CommonApiResponse<String>> handleRecordAlreadyExistsException(RecordAlreadyExistsException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder().errorCode(HttpStatus.BAD_REQUEST.value()).errorMessage(ex.getMessage()).build();
        CommonApiResponse<String> commonApiResponse = CommonApiResponse.<String>builder().errorDetails(errorDetails).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonApiResponse);
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

