package com.prunny.jwt_project.exceptions;

public class UsernameExistsException extends JwtProjectBaseException {

    public UsernameExistsException(String message) {
        super(message);
    }
}
