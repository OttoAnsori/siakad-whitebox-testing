package com.praktikum.service;

import com.praktikum.model.CourseGrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test untuk GradeCalculator (TANPA Mock/Stub)
 * Target Coverage: 95-100%
 */
@DisplayName("GradeCalculator Unit Tests")
class GradeCalculatorTest {

    private GradeCalculator gradeCalculator;

    @BeforeEach
    void setUp() {
        gradeCalculator = new GradeCalculator();
    }

    // ==================== calculateGPA() Tests ====================

    @Test
    @DisplayName("calculateGPA - Should return 0.0 when grades list is null")
    void testCalculateGPA_NullGrades() {
        double result = gradeCalculator.calculateGPA(null);
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("calculateGPA - Should return 0.0 when grades list is empty")
    void testCalculateGPA_EmptyGrades() {
        List<CourseGrade> grades = new ArrayList<>();
        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("calculateGPA - Should calculate GPA correctly for single course")
    void testCalculateGPA_SingleCourse() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, 4.0) // A grade, 3 credits
        );
        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(4.0, result);
    }

    @Test
    @DisplayName("calculateGPA - Should calculate GPA correctly for multiple courses")
    void testCalculateGPA_MultipleCourses() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, 4.0), // A, 3 SKS = 12 points
                new CourseGrade("CS102", 3, 3.0), // B, 3 SKS = 9 points
                new CourseGrade("CS103", 2, 4.0)  // A, 2 SKS = 8 points
        );
        // Total: 29 points / 8 SKS = 3.625 -> rounded to 3.63 (NOT 3.62!)
        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(3.63, result); // FIXED: Changed from 3.62 to 3.63
    }

    @Test
    @DisplayName("calculateGPA - Should calculate GPA with all A grades")
    void testCalculateGPA_AllAGrades() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, 4.0),
                new CourseGrade("CS102", 3, 4.0),
                new CourseGrade("CS103", 2, 4.0)
        );
        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(4.0, result);
    }

    @Test
    @DisplayName("calculateGPA - Should calculate GPA with all E grades")
    void testCalculateGPA_AllEGrades() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, 0.0),
                new CourseGrade("CS102", 3, 0.0)
        );
        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("calculateGPA - Should calculate GPA with mixed grades")
    void testCalculateGPA_MixedGrades() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 4, 4.0), // A, 4 SKS = 16
                new CourseGrade("CS102", 3, 3.0), // B, 3 SKS = 9
                new CourseGrade("CS103", 2, 2.0), // C, 2 SKS = 4
                new CourseGrade("CS104", 2, 1.0)  // D, 2 SKS = 2
        );
        // Total: 31 points / 11 SKS = 2.818... -> rounded to 2.82
        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(2.82, result);
    }

    @Test
    @DisplayName("calculateGPA - Should round GPA to 2 decimal places")
    void testCalculateGPA_RoundingToTwoDecimals() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, 3.7),
                new CourseGrade("CS102", 3, 3.3)
        );
        // Total: (3.7*3 + 3.3*3) / 6 = 21/6 = 3.5
        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(3.5, result);
    }

    @Test
    @DisplayName("calculateGPA - Should throw IllegalArgumentException for negative grade point")
    void testCalculateGPA_NegativeGradePoint() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, -1.0)
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gradeCalculator.calculateGPA(grades)
        );

        assertTrue(exception.getMessage().contains("Invalid grade point"));
    }

    @Test
    @DisplayName("calculateGPA - Should throw IllegalArgumentException for grade point > 4.0")
    void testCalculateGPA_GradePointAbove4() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, 4.5)
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gradeCalculator.calculateGPA(grades)
        );

        assertTrue(exception.getMessage().contains("Invalid grade point"));
    }

    @Test
    @DisplayName("calculateGPA - Should handle boundary value 0.0")
    void testCalculateGPA_BoundaryZero() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, 0.0)
        );
        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("calculateGPA - Should handle boundary value 4.0")
    void testCalculateGPA_BoundaryFour() {
        List<CourseGrade> grades = Arrays.asList(
                new CourseGrade("CS101", 3, 4.0)
        );
        double result = gradeCalculator.calculateGPA(grades);
        assertEquals(4.0, result);
    }

    // ==================== determineAcademicStatus() Tests ====================

    @Test
    @DisplayName("determineAcademicStatus - Should throw exception for negative GPA")
    void testDetermineAcademicStatus_NegativeGPA() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gradeCalculator.determineAcademicStatus(-0.1, 3)
        );
        assertTrue(exception.getMessage().contains("GPA must be between 0 and 4.0"));
    }

    @Test
    @DisplayName("determineAcademicStatus - Should throw exception for GPA > 4.0")
    void testDetermineAcademicStatus_GPAAbove4() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gradeCalculator.determineAcademicStatus(4.1, 3)
        );
        assertTrue(exception.getMessage().contains("GPA must be between 0 and 4.0"));
    }

    @Test
    @DisplayName("determineAcademicStatus - Should throw exception for semester < 1")
    void testDetermineAcademicStatus_InvalidSemester() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gradeCalculator.determineAcademicStatus(3.0, 0)
        );
        assertTrue(exception.getMessage().contains("Semester must be positive"));
    }

    // Semester 1-2 Tests
    @ParameterizedTest
    @CsvSource({
            "2.0, 1, ACTIVE",
            "2.5, 1, ACTIVE",
            "3.0, 1, ACTIVE",
            "4.0, 2, ACTIVE"
    })
    @DisplayName("determineAcademicStatus - Semester 1-2 with GPA >= 2.0 should return ACTIVE")
    void testDetermineAcademicStatus_Semester1to2_Active(double gpa, int semester, String expected) {
        String result = gradeCalculator.determineAcademicStatus(gpa, semester);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
            "1.99, 1, PROBATION",
            "1.5, 1, PROBATION",
            "1.0, 2, PROBATION",
            "0.0, 2, PROBATION"
    })
    @DisplayName("determineAcademicStatus - Semester 1-2 with GPA < 2.0 should return PROBATION")
    void testDetermineAcademicStatus_Semester1to2_Probation(double gpa, int semester, String expected) {
        String result = gradeCalculator.determineAcademicStatus(gpa, semester);
        assertEquals(expected, result);
    }

    // Semester 3-4 Tests
    @ParameterizedTest
    @CsvSource({
            "2.25, 3, ACTIVE",
            "3.0, 3, ACTIVE",
            "4.0, 4, ACTIVE"
    })
    @DisplayName("determineAcademicStatus - Semester 3-4 with GPA >= 2.25 should return ACTIVE")
    void testDetermineAcademicStatus_Semester3to4_Active(double gpa, int semester, String expected) {
        String result = gradeCalculator.determineAcademicStatus(gpa, semester);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
            "2.0, 3, PROBATION",
            "2.24, 3, PROBATION",
            "2.1, 4, PROBATION"
    })
    @DisplayName("determineAcademicStatus - Semester 3-4 with GPA 2.0-2.24 should return PROBATION")
    void testDetermineAcademicStatus_Semester3to4_Probation(double gpa, int semester, String expected) {
        String result = gradeCalculator.determineAcademicStatus(gpa, semester);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
            "1.99, 3, SUSPENDED",
            "1.5, 3, SUSPENDED",
            "0.0, 4, SUSPENDED"
    })
    @DisplayName("determineAcademicStatus - Semester 3-4 with GPA < 2.0 should return SUSPENDED")
    void testDetermineAcademicStatus_Semester3to4_Suspended(double gpa, int semester, String expected) {
        String result = gradeCalculator.determineAcademicStatus(gpa, semester);
        assertEquals(expected, result);
    }

    // Semester 5+ Tests
    @ParameterizedTest
    @CsvSource({
            "2.5, 5, ACTIVE",
            "3.0, 6, ACTIVE",
            "4.0, 8, ACTIVE"
    })
    @DisplayName("determineAcademicStatus - Semester 5+ with GPA >= 2.5 should return ACTIVE")
    void testDetermineAcademicStatus_Semester5Plus_Active(double gpa, int semester, String expected) {
        String result = gradeCalculator.determineAcademicStatus(gpa, semester);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
            "2.0, 5, PROBATION",
            "2.49, 5, PROBATION",
            "2.3, 7, PROBATION"
    })
    @DisplayName("determineAcademicStatus - Semester 5+ with GPA 2.0-2.49 should return PROBATION")
    void testDetermineAcademicStatus_Semester5Plus_Probation(double gpa, int semester, String expected) {
        String result = gradeCalculator.determineAcademicStatus(gpa, semester);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @CsvSource({
            "1.99, 5, SUSPENDED",
            "1.5, 6, SUSPENDED",
            "0.0, 8, SUSPENDED"
    })
    @DisplayName("determineAcademicStatus - Semester 5+ with GPA < 2.0 should return SUSPENDED")
    void testDetermineAcademicStatus_Semester5Plus_Suspended(double gpa, int semester, String expected) {
        String result = gradeCalculator.determineAcademicStatus(gpa, semester);
        assertEquals(expected, result);
    }

    // ==================== calculateMaxCredits() Tests ====================

    @Test
    @DisplayName("calculateMaxCredits - Should throw exception for negative GPA")
    void testCalculateMaxCredits_NegativeGPA() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gradeCalculator.calculateMaxCredits(-0.1)
        );
        assertTrue(exception.getMessage().contains("GPA must be between 0 and 4.0"));
    }

    @Test
    @DisplayName("calculateMaxCredits - Should throw exception for GPA > 4.0")
    void testCalculateMaxCredits_GPAAbove4() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> gradeCalculator.calculateMaxCredits(4.1)
        );
        assertTrue(exception.getMessage().contains("GPA must be between 0 and 4.0"));
    }

    @ParameterizedTest
    @ValueSource(doubles = {3.0, 3.5, 4.0})
    @DisplayName("calculateMaxCredits - Should return 24 for GPA >= 3.0")
    void testCalculateMaxCredits_24Credits(double gpa) {
        int result = gradeCalculator.calculateMaxCredits(gpa);
        assertEquals(24, result);
    }

    @ParameterizedTest
    @ValueSource(doubles = {2.5, 2.75, 2.99})
    @DisplayName("calculateMaxCredits - Should return 21 for GPA 2.5-2.99")
    void testCalculateMaxCredits_21Credits(double gpa) {
        int result = gradeCalculator.calculateMaxCredits(gpa);
        assertEquals(21, result);
    }

    @ParameterizedTest
    @ValueSource(doubles = {2.0, 2.25, 2.49})
    @DisplayName("calculateMaxCredits - Should return 18 for GPA 2.0-2.49")
    void testCalculateMaxCredits_18Credits(double gpa) {
        int result = gradeCalculator.calculateMaxCredits(gpa);
        assertEquals(18, result);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 1.0, 1.99})
    @DisplayName("calculateMaxCredits - Should return 15 for GPA < 2.0")
    void testCalculateMaxCredits_15Credits(double gpa) {
        int result = gradeCalculator.calculateMaxCredits(gpa);
        assertEquals(15, result);
    }

    @Test
    @DisplayName("calculateMaxCredits - Boundary test for GPA 3.0")
    void testCalculateMaxCredits_BoundaryGPA3() {
        assertEquals(24, gradeCalculator.calculateMaxCredits(3.0));
        assertEquals(21, gradeCalculator.calculateMaxCredits(2.99));
    }

    @Test
    @DisplayName("calculateMaxCredits - Boundary test for GPA 2.5")
    void testCalculateMaxCredits_BoundaryGPA2_5() {
        assertEquals(21, gradeCalculator.calculateMaxCredits(2.5));
        assertEquals(18, gradeCalculator.calculateMaxCredits(2.49));
    }

    @Test
    @DisplayName("calculateMaxCredits - Boundary test for GPA 2.0")
    void testCalculateMaxCredits_BoundaryGPA2() {
        assertEquals(18, gradeCalculator.calculateMaxCredits(2.0));
        assertEquals(15, gradeCalculator.calculateMaxCredits(1.99));
    }
}
