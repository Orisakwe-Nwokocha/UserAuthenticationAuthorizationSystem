package com.prunny.jwt_project.exceptions;

public abstract class JwtProjectBaseException extends RuntimeException {
    public JwtProjectBaseException(String message) {
        super(message);
    }
}
