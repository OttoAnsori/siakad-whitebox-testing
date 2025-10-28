package com.praktikum.exception;

/**
 * Exception yang dilempar ketika terjadi kesalahan dalam proses enrollment
 */
public class EnrollmentException extends RuntimeException {
    public EnrollmentException(String message) {
        super(message);
    }

    public EnrollmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
