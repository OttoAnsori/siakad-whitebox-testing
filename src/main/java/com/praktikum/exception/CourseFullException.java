package com.praktikum.exception;

/**
 * Exception yang dilempar ketika mata kuliah sudah penuh
 */
public class CourseFullException extends RuntimeException {
    public CourseFullException(String message) {
        super(message);
    }

    public CourseFullException(String message, Throwable cause) {
        super(message, cause);
    }
}
