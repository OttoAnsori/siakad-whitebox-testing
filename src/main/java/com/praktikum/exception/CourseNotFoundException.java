package com.praktikum.exception;

/**
 * Exception yang dilempar ketika mata kuliah tidak ditemukan
 */
public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String message) {
        super(message);
    }

    public CourseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
