package com.example.demo.exception;

import com.example.demo.exception.custom.ServiceUnavailableException;
import com.example.demo.exception.custom.CepNotFoundException;
import com.example.demo.exception.custom.InvalidCepFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCepFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCepFormat(InvalidCepFormatException ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CepNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCepNotFound(CepNotFoundException ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Map<String, String>> handleServiceUnavailable(ServiceUnavailableException ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
