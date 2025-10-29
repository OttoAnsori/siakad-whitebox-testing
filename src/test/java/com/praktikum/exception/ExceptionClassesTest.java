package com.praktikum.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Exception Classes Unit Tests")
class ExceptionClassesTest {

    @Test
    @DisplayName("CourseFullException - Should create with message")
    void testCourseFullExceptionWithMessage() {
        String message = "Course is full";
        CourseFullException exception = new CourseFullException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("CourseFullException - Should create with message and cause")
    void testCourseFullExceptionWithMessageAndCause() {
        String message = "Course is full";
        Throwable cause = new RuntimeException("Original cause");
        CourseFullException exception = new CourseFullException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("CourseFullException - Should be throwable")
    void testCourseFullExceptionThrowable() {
        assertThrows(CourseFullException.class, () -> {
            throw new CourseFullException("Course capacity reached");
        });
    }

    // ==================== CourseNotFoundException Tests ====================

    @Test
    @DisplayName("CourseNotFoundException - Should create with message")
    void testCourseNotFoundExceptionWithMessage() {
        String message = "Course not found";
        CourseNotFoundException exception = new CourseNotFoundException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("CourseNotFoundException - Should create with message and cause")
    void testCourseNotFoundExceptionWithMessageAndCause() {
        String message = "Course not found";
        Throwable cause = new RuntimeException("Database error");
        CourseNotFoundException exception = new CourseNotFoundException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("CourseNotFoundException - Should be throwable")
    void testCourseNotFoundExceptionThrowable() {
        assertThrows(CourseNotFoundException.class, () -> {
            throw new CourseNotFoundException("CS999 not found");
        });
    }

    // ==================== EnrollmentException Tests ====================

    @Test
    @DisplayName("EnrollmentException - Should create with message")
    void testEnrollmentExceptionWithMessage() {
        String message = "Enrollment failed";
        EnrollmentException exception = new EnrollmentException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("EnrollmentException - Should create with message and cause")
    void testEnrollmentExceptionWithMessageAndCause() {
        String message = "Enrollment failed";
        Throwable cause = new RuntimeException("System error");
        EnrollmentException exception = new EnrollmentException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("EnrollmentException - Should be throwable")
    void testEnrollmentExceptionThrowable() {
        assertThrows(EnrollmentException.class, () -> {
            throw new EnrollmentException("Student is suspended");
        });
    }

    // ==================== PrerequisiteNotMetException Tests ====================

    @Test
    @DisplayName("PrerequisiteNotMetException - Should create with message")
    void testPrerequisiteNotMetExceptionWithMessage() {
        String message = "Prerequisites not met";
        PrerequisiteNotMetException exception = new PrerequisiteNotMetException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("PrerequisiteNotMetException - Should create with message and cause")
    void testPrerequisiteNotMetExceptionWithMessageAndCause() {
        String message = "Prerequisites not met";
        Throwable cause = new RuntimeException("Validation error");
        PrerequisiteNotMetException exception = new PrerequisiteNotMetException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("PrerequisiteNotMetException - Should be throwable")
    void testPrerequisiteNotMetExceptionThrowable() {
        assertThrows(PrerequisiteNotMetException.class, () -> {
            throw new PrerequisiteNotMetException("CS101 required");
        });
    }

    // ==================== StudentNotFoundException Tests ====================

    @Test
    @DisplayName("StudentNotFoundException - Should create with message")
    void testStudentNotFoundExceptionWithMessage() {
        String message = "Student not found";
        StudentNotFoundException exception = new StudentNotFoundException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("StudentNotFoundException - Should create with message and cause")
    void testStudentNotFoundExceptionWithMessageAndCause() {
        String message = "Student not found";
        Throwable cause = new RuntimeException("Database connection lost");
        StudentNotFoundException exception = new StudentNotFoundException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("StudentNotFoundException - Should be throwable")
    void testStudentNotFoundExceptionThrowable() {
        assertThrows(StudentNotFoundException.class, () -> {
            throw new StudentNotFoundException("S999 not found");
        });
    }
}
