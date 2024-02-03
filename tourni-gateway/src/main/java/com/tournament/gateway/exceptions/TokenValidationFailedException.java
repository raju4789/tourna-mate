package com.tournament.gateway.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class TokenValidationFailedException extends RuntimeException {

    private HttpStatus status;

    public TokenValidationFailedException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
