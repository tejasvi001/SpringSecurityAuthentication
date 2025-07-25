package com.example.security.advices;


import com.example.security.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIError> handleResourceNotFoundException(ResourceNotFoundException exception){
        APIError apiError=new APIError(HttpStatus.NOT_FOUND,exception.getMessage());
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public ResponseEntity<?> handleException(Exception ex) {
//        Map<String, String> error = new HashMap<>();
//        error.put("error", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<APIError> handleAuthException(AuthenticationException ex){
        APIError apiError=new APIError( HttpStatus.UNAUTHORIZED, ex.getMessage());
        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(JwtException.class)
    public ResponseEntity<APIError> handleJwtException(JwtException ex){
        APIError apiError=new APIError( HttpStatus.UNAUTHORIZED, ex.getMessage());
        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIError> handleAccessDeniedException(AccessDeniedException ex){
        APIError apiError=new APIError(HttpStatus.FORBIDDEN, ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError,HttpStatus.FORBIDDEN);
    }
}
