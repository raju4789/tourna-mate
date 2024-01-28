package com.tournament.pointstabletracker.advice;

import com.tournament.pointstabletracker.dto.CommonApiResponse;
import com.tournament.pointstabletracker.dto.ErrorDetails;
import com.tournament.pointstabletracker.exceptions.*;
import com.tournament.pointstabletracker.service.PointsTableTrackerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
        logger.error("RuntimeException occurred: ", ex);
        ErrorDetails errorDetails = ErrorDetails.builder().errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).errorMessage(ex.getMessage()).build();
        CommonApiResponse<String> commonApiResponse = CommonApiResponse.<String>builder().errorDetails(errorDetails).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonApiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonApiResponse<String>> handleDefaultException(Exception ex) {
        logger.error("Exception occurred: ", ex);
        ErrorDetails errorDetails = ErrorDetails.builder().errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).errorMessage(ex.getMessage()).build();
        CommonApiResponse<String> commonApiResponse = CommonApiResponse.<String>builder().errorDetails(errorDetails).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonApiResponse);
    }
}

