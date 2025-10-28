package com.praktikum.exception;

/**
 * Exception yang dilempar ketika mahasiswa tidak ditemukan
 */
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }

    public StudentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
