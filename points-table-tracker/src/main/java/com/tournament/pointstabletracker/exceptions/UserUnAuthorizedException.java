package com.tournament.pointstabletracker.exceptions;

public class UserUnAuthorizedException extends RuntimeException{
    public UserUnAuthorizedException(String message) {
        super(message);
    }
}
