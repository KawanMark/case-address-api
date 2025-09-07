package com.example.demo.exception.custom;

public class InvalidCepFormatException extends RuntimeException {
    public InvalidCepFormatException(String message) {
        super(message);
    }
}
