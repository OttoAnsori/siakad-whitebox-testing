package com.praktikum.service;

import com.praktikum.exception.CourseNotFoundException;
import com.praktikum.exception.StudentNotFoundException;
import com.praktikum.model.Course;
import com.praktikum.model.Student;
import com.praktikum.repository.CourseRepository;
import com.praktikum.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test untuk EnrollmentService menggunakan STUB
 * STUB = Manual implementation dari interface repository
 * Target Coverage: 85-90% untuk method validateCreditLimit() dan dropCourse()
 */
@DisplayName("EnrollmentService Tests with STUB")
class EnrollmentServiceTestWithStub {

    private EnrollmentService enrollmentService;
    private StudentRepositoryStub studentRepositoryStub;
    private CourseRepositoryStub courseRepositoryStub;
    private NotificationServiceStub notificationServiceStub;
    private GradeCalculator gradeCalculator;

    // ==================== STUB IMPLEMENTATIONS ====================

    /**
     * STUB untuk StudentRepository
     * Ini adalah manual implementation untuk testing
     */
    class StudentRepositoryStub implements StudentRepository {
        private Map<String, Student> students = new HashMap<>();

        public void addStudent(Student student) {
            students.put(student.getStudentId(), student);
        }

        @Override
        public Student findById(String studentId) {
            return students.get(studentId);
        }

        @Override
        public void update(Student student) {
            students.put(student.getStudentId(), student);
        }

        @Override
        public List<Course> getCompletedCourses(String studentId) {
            return new ArrayList<>();
        }
    }

    /**
     * STUB untuk CourseRepository
     */
    class CourseRepositoryStub implements CourseRepository {
        private Map<String, Course> courses = new HashMap<>();

        public void addCourse(Course course) {
            courses.put(course.getCourseCode(), course);
        }

        @Override
        public Course findByCourseCode(String courseCode) {
            return courses.get(courseCode);
        }

        @Override
        public void update(Course course) {
            courses.put(course.getCourseCode(), course);
        }

        @Override
        public boolean isPrerequisiteMet(String studentId, String courseCode) {
            return true;
        }
    }

    /**
     * STUB untuk NotificationService
     */
    class NotificationServiceStub implements NotificationService {
        public int emailSentCount = 0;
        public int smsSentCount = 0;
        public String lastEmailSubject;
        public String lastEmailMessage;

        @Override
        public void sendEmail(String email, String subject, String message) {
            emailSentCount++;
            lastEmailSubject = subject;
            lastEmailMessage = message;
        }

        @Override
        public void sendSMS(String phone, String message) {
            smsSentCount++;
        }
    }

    @BeforeEach
    void setUp() {
        studentRepositoryStub = new StudentRepositoryStub();
        courseRepositoryStub = new CourseRepositoryStub();
        notificationServiceStub = new NotificationServiceStub();
        gradeCalculator = new GradeCalculator();

        enrollmentService = new EnrollmentService(
                studentRepositoryStub,
                courseRepositoryStub,
                notificationServiceStub,
                gradeCalculator
        );
    }

    // ==================== validateCreditLimit() Tests with STUB ====================

    @Test
    @DisplayName("STUB - validateCreditLimit should return true when credits within limit")
    void testValidateCreditLimit_WithinLimit() {
        // Arrange: Buat student dengan GPA 3.5 (max 24 SKS)
        Student student = new Student("S001", "John Doe", "john@email.com",
                "Computer Science", 3, 3.5, "ACTIVE");
        studentRepositoryStub.addStudent(student);

        // Act: Request 20 SKS (dalam batas 24)
        boolean result = enrollmentService.validateCreditLimit("S001", 20);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("STUB - validateCreditLimit should return false when credits exceed limit")
    void testValidateCreditLimit_ExceedLimit() {
        // Arrange: Buat student dengan GPA 2.3 (max 18 SKS)
        Student student = new Student("S002", "Jane Smith", "jane@email.com",
                "Information Systems", 4, 2.3, "ACTIVE");
        studentRepositoryStub.addStudent(student);

        // Act: Request 21 SKS (melebihi batas 18)
        boolean result = enrollmentService.validateCreditLimit("S002", 21);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("STUB - validateCreditLimit should return true when credits exactly at limit")
    void testValidateCreditLimit_ExactlyAtLimit() {
        // Arrange: Buat student dengan GPA 3.0 (max 24 SKS)
        Student student = new Student("S003", "Bob Wilson", "bob@email.com",
                "Software Engineering", 5, 3.0, "ACTIVE");
        studentRepositoryStub.addStudent(student);

        // Act: Request exactly 24 SKS
        boolean result = enrollmentService.validateCreditLimit("S003", 24);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("STUB - validateCreditLimit should throw exception when student not found")
    void testValidateCreditLimit_StudentNotFound() {
        // Act & Assert
        assertThrows(StudentNotFoundException.class, () -> {
            enrollmentService.validateCreditLimit("INVALID_ID", 20);
        });
    }

    @Test
    @DisplayName("STUB - validateCreditLimit with GPA 3.5 should allow 24 credits")
    void testValidateCreditLimit_HighGPA() {
        // Arrange
        Student student = new Student("S004", "Alice Brown", "alice@email.com",
                "Data Science", 4, 3.8, "ACTIVE");
        studentRepositoryStub.addStudent(student);

        // Act & Assert
        assertTrue(enrollmentService.validateCreditLimit("S004", 24));
        assertFalse(enrollmentService.validateCreditLimit("S004", 25));
    }

    @Test
    @DisplayName("STUB - validateCreditLimit with GPA 2.7 should allow 21 credits")
    void testValidateCreditLimit_MediumGPA() {
        // Arrange
        Student student = new Student("S005", "Charlie Davis", "charlie@email.com",
                "Cyber Security", 3, 2.7, "ACTIVE");
        studentRepositoryStub.addStudent(student);

        // Act & Assert
        assertTrue(enrollmentService.validateCreditLimit("S005", 21));
        assertFalse(enrollmentService.validateCreditLimit("S005", 22));
    }

    @Test
    @DisplayName("STUB - validateCreditLimit with GPA 2.3 should allow 18 credits")
    void testValidateCreditLimit_LowMediumGPA() {
        // Arrange
        Student student = new Student("S006", "Diana Evans", "diana@email.com",
                "Network Engineering", 5, 2.3, "ACTIVE");
        studentRepositoryStub.addStudent(student);

        // Act & Assert
        assertTrue(enrollmentService.validateCreditLimit("S006", 18));
        assertFalse(enrollmentService.validateCreditLimit("S006", 19));
    }

    @Test
    @DisplayName("STUB - validateCreditLimit with GPA 1.8 should allow 15 credits")
    void testValidateCreditLimit_LowGPA() {
        // Arrange
        Student student = new Student("S007", "Edward Foster", "edward@email.com",
                "Information Technology", 2, 1.8, "PROBATION");
        studentRepositoryStub.addStudent(student);

        // Act & Assert
        assertTrue(enrollmentService.validateCreditLimit("S007", 15));
        assertFalse(enrollmentService.validateCreditLimit("S007", 16));
    }

    // ==================== dropCourse() Tests with STUB ====================

    @Test
    @DisplayName("STUB - dropCourse should successfully drop course and update count")
    void testDropCourse_Success() {
        // Arrange
        Student student = new Student("S010", "Frank Green", "frank@email.com",
                "Computer Science", 3, 3.2, "ACTIVE");
        studentRepositoryStub.addStudent(student);

        Course course = new Course("CS201", "Data Structures", 3, 40, 35, "Dr. Smith");
        courseRepositoryStub.addCourse(course);

        // Act
        enrollmentService.dropCourse("S010", "CS201");

        // Assert
        Course updatedCourse = courseRepositoryStub.findByCourseCode("CS201");
        assertEquals(34, updatedCourse.getEnrolledCount());
    }

    @Test
    @DisplayName("STUB - dropCourse should send notification email")
    void testDropCourse_SendsNotification() {
        // Arrange
        Student student = new Student("S011", "Grace Harris", "grace@email.com",
                "Information Systems", 4, 3.0, "ACTIVE");
        studentRepositoryStub.addStudent(student);

        Course course = new Course("IS301", "Database Systems", 3, 35, 30, "Dr. Johnson");
        courseRepositoryStub.addCourse(course);

        // Act
        enrollmentService.dropCourse("S011", "IS301");

        // Assert
        assertEquals(1, notificationServiceStub.emailSentCount);
        assertEquals("Course Drop Confirmation", notificationServiceStub.lastEmailSubject);
        assertTrue(notificationServiceStub.lastEmailMessage.contains("Database Systems"));
    }

    @Test
    @DisplayName("STUB - dropCourse should throw exception when student not found")
    void testDropCourse_StudentNotFound() {
        // Arrange
        Course course = new Course("CS101", "Programming", 3, 40, 25, "Dr. Brown");
        courseRepositoryStub.addCourse(course);

        // Act & Assert
        assertThrows(StudentNotFoundException.class, () -> {
            enrollmentService.dropCourse("INVALID_STUDENT", "CS101");
        });
    }

    @Test
    @DisplayName("STUB - dropCourse should throw exception when course not found")
    void testDropCourse_CourseNotFound() {
        // Arrange
        Student student = new Student("S012", "Henry Irving", "henry@email.com",
                "Software Engineering", 3, 3.5, "ACTIVE");
        studentRepositoryStub.addStudent(student);

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> {
            enrollmentService.dropCourse("S012", "INVALID_COURSE");
        });
    }

    @Test
    @DisplayName("STUB - dropCourse should handle course with 1 enrolled student")
    void testDropCourse_SingleStudent() {
        // Arrange
        Student student = new Student("S013", "Ivy Jackson", "ivy@email.com",
                "Data Science", 2, 3.8, "ACTIVE");
        studentRepositoryStub.addStudent(student);

        Course course = new Course("DS101", "Introduction to Data Science", 3, 30, 1, "Dr. Lee");
        courseRepositoryStub.addCourse(course);

        // Act
        enrollmentService.dropCourse("S013", "DS101");

        // Assert
        Course updatedCourse = courseRepositoryStub.findByCourseCode("DS101");
        assertEquals(0, updatedCourse.getEnrolledCount());
    }

    @Test
    @DisplayName("STUB - dropCourse should work for multiple courses")
    void testDropCourse_MultipleCourses() {
        // Arrange
        Student student = new Student("S014", "Jack King", "jack@email.com",
                "Cyber Security", 5, 2.9, "ACTIVE");
        studentRepositoryStub.addStudent(student);

        Course course1 = new Course("CS301", "Operating Systems", 3, 35, 28, "Dr. White");
        Course course2 = new Course("CS302", "Computer Networks", 3, 35, 30, "Dr. Black");
        courseRepositoryStub.addCourse(course1);
        courseRepositoryStub.addCourse(course2);

        // Act
        enrollmentService.dropCourse("S014", "CS301");
        enrollmentService.dropCourse("S014", "CS302");

        // Assert
        assertEquals(27, courseRepositoryStub.findByCourseCode("CS301").getEnrolledCount());
        assertEquals(29, courseRepositoryStub.findByCourseCode("CS302").getEnrolledCount());
        assertEquals(2, notificationServiceStub.emailSentCount);
    }

    @Test
    @DisplayName("STUB - dropCourse should update repository correctly")
    void testDropCourse_RepositoryUpdate() {
        // Arrange
        Student student = new Student("S015", "Karen Lee", "karen@email.com",
                "Network Engineering", 4, 3.3, "ACTIVE");
        studentRepositoryStub.addStudent(student);

        Course course = new Course("NET401", "Advanced Networking", 4, 25, 20, "Dr. Gray");
        courseRepositoryStub.addCourse(course);

        int initialCount = course.getEnrolledCount();

        // Act
        enrollmentService.dropCourse("S015", "NET401");

        // Assert
        Course updatedCourse = courseRepositoryStub.findByCourseCode("NET401");
        assertEquals(initialCount - 1, updatedCourse.getEnrolledCount());
    }
}
