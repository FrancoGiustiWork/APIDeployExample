package org.example.exeption;

public class DnaHashCalculationException extends RuntimeException {
    public DnaHashCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}