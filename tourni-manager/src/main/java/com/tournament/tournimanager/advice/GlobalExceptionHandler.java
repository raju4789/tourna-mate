package com.tournament.tournimanager.advice;


import com.tournament.tournimanager.dto.CommonApiResponse;
import com.tournament.tournimanager.dto.ErrorDetails;
import com.tournament.tournimanager.exceptions.InvalidRequestException;
import com.tournament.tournimanager.exceptions.RecordAlreadyExistsException;
import com.tournament.tournimanager.exceptions.RecordNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;


import java.util.List;

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

