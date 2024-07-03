package com.prunny.jwt_project.handlers;

import com.prunny.jwt_project.dto.responses.ErrorResponse;
import com.prunny.jwt_project.exceptions.UsernameExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.time.LocalDateTime.now;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> handleNullPointerException(NullPointerException exception,
                                                        HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .requestTime(now())
                .success(false)
                .error("IllegalState")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<?> handleUsernameExistsException(UsernameExistsException exception,
                                                        HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .requestTime(now())
                .success(false)
                .error("UsernameExists")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.badRequest().body(response);
    }
}
