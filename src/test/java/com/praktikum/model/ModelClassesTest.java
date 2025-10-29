package com.praktikum.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Model Classes Unit Tests")
class ModelClassesTest {

    @Test
    @DisplayName("Student - Should create student with constructor")
    void testStudentConstructor() {
        Student student = new Student("S001", "John Doe", "john@email.com",
                "Computer Science", 3, 3.5, "ACTIVE");

        assertEquals("S001", student.getStudentId());
        assertEquals("John Doe", student.getName());
        assertEquals("john@email.com", student.getEmail());
        assertEquals("Computer Science", student.getMajor());
        assertEquals(3, student.getSemester());
        assertEquals(3.5, student.getGpa());
        assertEquals("ACTIVE", student.getAcademicStatus());
    }

    @Test
    @DisplayName("Student - Should create student with default constructor")
    void testStudentDefaultConstructor() {
        Student student = new Student();
        assertNotNull(student);
    }

    @Test
    @DisplayName("Student - Should set and get studentId")
    void testStudentSetGetStudentId() {
        Student student = new Student();
        student.setStudentId("S100");
        assertEquals("S100", student.getStudentId());
    }

    @Test
    @DisplayName("Student - Should set and get name")
    void testStudentSetGetName() {
        Student student = new Student();
        student.setName("Alice Brown");
        assertEquals("Alice Brown", student.getName());
    }

    @Test
    @DisplayName("Student - Should set and get email")
    void testStudentSetGetEmail() {
        Student student = new Student();
        student.setEmail("alice@test.com");
        assertEquals("alice@test.com", student.getEmail());
    }

    @Test
    @DisplayName("Student - Should set and get major")
    void testStudentSetGetMajor() {
        Student student = new Student();
        student.setMajor("Data Science");
        assertEquals("Data Science", student.getMajor());
    }

    @Test
    @DisplayName("Student - Should set and get semester")
    void testStudentSetGetSemester() {
        Student student = new Student();
        student.setSemester(5);
        assertEquals(5, student.getSemester());
    }

    @Test
    @DisplayName("Student - Should set and get GPA")
    void testStudentSetGetGpa() {
        Student student = new Student();
        student.setGpa(3.75);
        assertEquals(3.75, student.getGpa());
    }

    @Test
    @DisplayName("Student - Should set and get academic status")
    void testStudentSetGetAcademicStatus() {
        Student student = new Student();
        student.setAcademicStatus("PROBATION");
        assertEquals("PROBATION", student.getAcademicStatus());
    }

    // ==================== Course Model Tests ====================

    @Test
    @DisplayName("Course - Should create course with constructor")
    void testCourseConstructor() {
        Course course = new Course("CS101", "Introduction to Programming", 3,
                40, 35, "Dr. Smith");

        assertEquals("CS101", course.getCourseCode());
        assertEquals("Introduction to Programming", course.getCourseName());
        assertEquals(3, course.getCredits());
        assertEquals(40, course.getCapacity());
        assertEquals(35, course.getEnrolledCount());
        assertEquals("Dr. Smith", course.getLecturer());
        assertNotNull(course.getPrerequisites());
    }

    @Test
    @DisplayName("Course - Should create course with default constructor")
    void testCourseDefaultConstructor() {
        Course course = new Course();
        assertNotNull(course);
        assertNotNull(course.getPrerequisites());
    }

    @Test
    @DisplayName("Course - Should set and get courseCode")
    void testCourseSetGetCourseCode() {
        Course course = new Course();
        course.setCourseCode("CS202");
        assertEquals("CS202", course.getCourseCode());
    }

    @Test
    @DisplayName("Course - Should set and get courseName")
    void testCourseSetGetCourseName() {
        Course course = new Course();
        course.setCourseName("Data Structures");
        assertEquals("Data Structures", course.getCourseName());
    }

    @Test
    @DisplayName("Course - Should set and get credits")
    void testCourseSetGetCredits() {
        Course course = new Course();
        course.setCredits(4);
        assertEquals(4, course.getCredits());
    }

    @Test
    @DisplayName("Course - Should set and get capacity")
    void testCourseSetGetCapacity() {
        Course course = new Course();
        course.setCapacity(50);
        assertEquals(50, course.getCapacity());
    }

    @Test
    @DisplayName("Course - Should set and get enrolledCount")
    void testCourseSetGetEnrolledCount() {
        Course course = new Course();
        course.setEnrolledCount(25);
        assertEquals(25, course.getEnrolledCount());
    }

    @Test
    @DisplayName("Course - Should set and get lecturer")
    void testCourseSetGetLecturer() {
        Course course = new Course();
        course.setLecturer("Dr. Johnson");
        assertEquals("Dr. Johnson", course.getLecturer());
    }

    @Test
    @DisplayName("Course - Should set and get prerequisites list")
    void testCourseSetGetPrerequisites() {
        Course course = new Course();
        List<String> prereqs = List.of("CS101", "CS102");
        course.setPrerequisites(prereqs);
        assertEquals(2, course.getPrerequisites().size());
    }

    @Test
    @DisplayName("Course - Should add prerequisite")
    void testCourseAddPrerequisite() {
        Course course = new Course();
        course.addPrerequisite("CS101");
        course.addPrerequisite("CS102");

        assertEquals(2, course.getPrerequisites().size());
        assertTrue(course.getPrerequisites().contains("CS101"));
        assertTrue(course.getPrerequisites().contains("CS102"));
    }

    // ==================== Enrollment Model Tests ====================

    @Test
    @DisplayName("Enrollment - Should create enrollment with constructor")
    void testEnrollmentConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Enrollment enrollment = new Enrollment("ENR001", "S001", "CS101", now, "APPROVED");

        assertEquals("ENR001", enrollment.getEnrollmentId());
        assertEquals("S001", enrollment.getStudentId());
        assertEquals("CS101", enrollment.getCourseCode());
        assertEquals(now, enrollment.getEnrollmentDate());
        assertEquals("APPROVED", enrollment.getStatus());
    }

    @Test
    @DisplayName("Enrollment - Should create enrollment with default constructor")
    void testEnrollmentDefaultConstructor() {
        Enrollment enrollment = new Enrollment();
        assertNotNull(enrollment);
    }

    @Test
    @DisplayName("Enrollment - Should set and get enrollmentId")
    void testEnrollmentSetGetEnrollmentId() {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId("ENR123");
        assertEquals("ENR123", enrollment.getEnrollmentId());
    }

    @Test
    @DisplayName("Enrollment - Should set and get studentId")
    void testEnrollmentSetGetStudentId() {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId("S999");
        assertEquals("S999", enrollment.getStudentId());
    }

    @Test
    @DisplayName("Enrollment - Should set and get courseCode")
    void testEnrollmentSetGetCourseCode() {
        Enrollment enrollment = new Enrollment();
        enrollment.setCourseCode("CS999");
        assertEquals("CS999", enrollment.getCourseCode());
    }

    @Test
    @DisplayName("Enrollment - Should set and get enrollmentDate")
    void testEnrollmentSetGetEnrollmentDate() {
        Enrollment enrollment = new Enrollment();
        LocalDateTime date = LocalDateTime.of(2025, 10, 28, 10, 30);
        enrollment.setEnrollmentDate(date);
        assertEquals(date, enrollment.getEnrollmentDate());
    }

    @Test
    @DisplayName("Enrollment - Should set and get status")
    void testEnrollmentSetGetStatus() {
        Enrollment enrollment = new Enrollment();
        enrollment.setStatus("PENDING");
        assertEquals("PENDING", enrollment.getStatus());
    }

    // ==================== CourseGrade Model Tests ====================

    @Test
    @DisplayName("CourseGrade - Should create courseGrade with constructor")
    void testCourseGradeConstructor() {
        CourseGrade grade = new CourseGrade("CS101", 3, 4.0);

        assertEquals("CS101", grade.getCourseCode());
        assertEquals(3, grade.getCredits());
        assertEquals(4.0, grade.getGradePoint());
    }

    @Test
    @DisplayName("CourseGrade - Should create courseGrade with default constructor")
    void testCourseGradeDefaultConstructor() {
        CourseGrade grade = new CourseGrade();
        assertNotNull(grade);
    }

    @Test
    @DisplayName("CourseGrade - Should set and get courseCode")
    void testCourseGradeSetGetCourseCode() {
        CourseGrade grade = new CourseGrade();
        grade.setCourseCode("CS202");
        assertEquals("CS202", grade.getCourseCode());
    }

    @Test
    @DisplayName("CourseGrade - Should set and get credits")
    void testCourseGradeSetGetCredits() {
        CourseGrade grade = new CourseGrade();
        grade.setCredits(4);
        assertEquals(4, grade.getCredits());
    }

    @Test
    @DisplayName("CourseGrade - Should set and get gradePoint")
    void testCourseGradeSetGetGradePoint() {
        CourseGrade grade = new CourseGrade();
        grade.setGradePoint(3.5);
        assertEquals(3.5, grade.getGradePoint());
    }

    @Test
    @DisplayName("CourseGrade - Should handle grade point 0.0")
    void testCourseGradeZeroGradePoint() {
        CourseGrade grade = new CourseGrade("CS303", 3, 0.0);
        assertEquals(0.0, grade.getGradePoint());
    }

    @Test
    @DisplayName("CourseGrade - Should handle grade point 4.0")
    void testCourseGradeMaxGradePoint() {
        CourseGrade grade = new CourseGrade("CS404", 3, 4.0);
        assertEquals(4.0, grade.getGradePoint());
    }
}
