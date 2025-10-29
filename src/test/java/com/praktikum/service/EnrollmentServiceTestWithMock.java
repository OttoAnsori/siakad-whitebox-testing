package com.praktikum.service;

import com.praktikum.exception.*;
import com.praktikum.model.Course;
import com.praktikum.model.Enrollment;
import com.praktikum.model.Student;
import com.praktikum.repository.CourseRepository;
import com.praktikum.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EnrollmentService Tests with MOCKITO")
class EnrollmentServiceTestWithMock {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private GradeCalculator gradeCalculator;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Captor
    private ArgumentCaptor<Course> courseCaptor;

    @Captor
    private ArgumentCaptor<String> emailCaptor;

    private Student testStudent;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testStudent = new Student("S001", "John Doe", "john@email.com",
                "Computer Science", 3, 3.5, "ACTIVE");
        testCourse = new Course("CS301", "Algorithm Design", 3, 40, 30, "Dr. Smith");
    }

    // ==================== enrollCourse() Success Tests ====================

    @Test
    @DisplayName("MOCK - enrollCourse should successfully enroll student in course")
    void testEnrollCourse_Success() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS301")).thenReturn(testCourse);
        when(courseRepository.isPrerequisiteMet("S001", "CS301")).thenReturn(true);

        Enrollment result = enrollmentService.enrollCourse("S001", "CS301");

        assertNotNull(result);
        assertEquals("S001", result.getStudentId());
        assertEquals("CS301", result.getCourseCode());
        assertEquals("APPROVED", result.getStatus());
        assertNotNull(result.getEnrollmentId());
        assertNotNull(result.getEnrollmentDate());

        verify(studentRepository, times(1)).findById("S001");
        verify(courseRepository, times(1)).findByCourseCode("CS301");
        verify(courseRepository, times(1)).isPrerequisiteMet("S001", "CS301");
        verify(courseRepository, times(1)).update(any(Course.class));
        verify(notificationService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("MOCK - enrollCourse should increment enrolled count")
    void testEnrollCourse_IncrementEnrolledCount() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS301")).thenReturn(testCourse);
        when(courseRepository.isPrerequisiteMet("S001", "CS301")).thenReturn(true);

        int initialCount = testCourse.getEnrolledCount();

        enrollmentService.enrollCourse("S001", "CS301");

        verify(courseRepository).update(courseCaptor.capture());
        Course updatedCourse = courseCaptor.getValue();
        assertEquals(initialCount + 1, updatedCourse.getEnrolledCount());
    }

    @Test
    @DisplayName("MOCK - enrollCourse should send confirmation email with correct details")
    void testEnrollCourse_SendsConfirmationEmail() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS301")).thenReturn(testCourse);
        when(courseRepository.isPrerequisiteMet("S001", "CS301")).thenReturn(true);

        enrollmentService.enrollCourse("S001", "CS301");

        verify(notificationService).sendEmail(
                eq("john@email.com"),
                eq("Enrollment Confirmation"),
                contains("Algorithm Design")
        );
    }

    // ==================== enrollCourse() Exception Tests ====================

    @Test
    @DisplayName("MOCK - enrollCourse should throw StudentNotFoundException when student not found")
    void testEnrollCourse_StudentNotFound() {
        when(studentRepository.findById("INVALID")).thenReturn(null);

        StudentNotFoundException exception = assertThrows(
                StudentNotFoundException.class,
                () -> enrollmentService.enrollCourse("INVALID", "CS301")
        );

        assertTrue(exception.getMessage().contains("Student not found"));
        verify(studentRepository, times(1)).findById("INVALID");
        verify(courseRepository, never()).findByCourseCode(anyString());
        verify(notificationService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("MOCK - enrollCourse should throw EnrollmentException when student is suspended")
    void testEnrollCourse_StudentSuspended() {
        Student suspendedStudent = new Student("S002", "Jane Smith", "jane@email.com",
                "Information Systems", 5, 1.5, "SUSPENDED");
        when(studentRepository.findById("S002")).thenReturn(suspendedStudent);

        EnrollmentException exception = assertThrows(
                EnrollmentException.class,
                () -> enrollmentService.enrollCourse("S002", "CS301")
        );

        assertTrue(exception.getMessage().contains("suspended"));
        verify(studentRepository, times(1)).findById("S002");
        verify(courseRepository, never()).findByCourseCode(anyString());
        verify(notificationService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("MOCK - enrollCourse should allow PROBATION students to enroll")
    void testEnrollCourse_StudentOnProbation() {
        Student probationStudent = new Student("S003", "Bob Wilson", "bob@email.com",
                "Software Engineering", 4, 2.1, "PROBATION");
        when(studentRepository.findById("S003")).thenReturn(probationStudent);
        when(courseRepository.findByCourseCode("CS301")).thenReturn(testCourse);
        when(courseRepository.isPrerequisiteMet("S003", "CS301")).thenReturn(true);

        Enrollment result = enrollmentService.enrollCourse("S003", "CS301");

        assertNotNull(result);
        assertEquals("APPROVED", result.getStatus());
        verify(notificationService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("MOCK - enrollCourse should throw CourseNotFoundException when course not found")
    void testEnrollCourse_CourseNotFound() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("INVALID")).thenReturn(null);

        CourseNotFoundException exception = assertThrows(
                CourseNotFoundException.class,
                () -> enrollmentService.enrollCourse("S001", "INVALID")
        );

        assertTrue(exception.getMessage().contains("Course not found"));
        verify(studentRepository, times(1)).findById("S001");
        verify(courseRepository, times(1)).findByCourseCode("INVALID");
        verify(courseRepository, never()).update(any(Course.class));
        verify(notificationService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("MOCK - enrollCourse should throw CourseFullException when course is full")
    void testEnrollCourse_CourseFull() {
        Course fullCourse = new Course("CS301", "Algorithm Design", 3, 40, 40, "Dr. Smith");
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS301")).thenReturn(fullCourse);

        CourseFullException exception = assertThrows(
                CourseFullException.class,
                () -> enrollmentService.enrollCourse("S001", "CS301")
        );

        assertTrue(exception.getMessage().contains("full"));
        verify(studentRepository, times(1)).findById("S001");
        verify(courseRepository, times(1)).findByCourseCode("CS301");
        verify(courseRepository, never()).isPrerequisiteMet(anyString(), anyString());
        verify(courseRepository, never()).update(any(Course.class));
        verify(notificationService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("MOCK - enrollCourse should throw CourseFullException when enrolled equals capacity")
    void testEnrollCourse_CourseExactlyFull() {
        Course fullCourse = new Course("CS302", "Database", 3, 35, 35, "Dr. Lee");
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS302")).thenReturn(fullCourse);

        assertThrows(CourseFullException.class,
                () -> enrollmentService.enrollCourse("S001", "CS302"));

        verify(courseRepository, never()).update(any(Course.class));
    }

    @Test
    @DisplayName("MOCK - enrollCourse should succeed when course has one slot left")
    void testEnrollCourse_CourseOneSlotLeft() {
        Course almostFullCourse = new Course("CS303", "Software Testing", 3, 40, 39, "Dr. Brown");
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS303")).thenReturn(almostFullCourse);
        when(courseRepository.isPrerequisiteMet("S001", "CS303")).thenReturn(true);

        Enrollment result = enrollmentService.enrollCourse("S001", "CS303");

        assertNotNull(result);
        verify(courseRepository).update(courseCaptor.capture());
        assertEquals(40, courseCaptor.getValue().getEnrolledCount());
    }

    @Test
    @DisplayName("MOCK - enrollCourse should throw PrerequisiteNotMetException when prerequisites not met")
    void testEnrollCourse_PrerequisiteNotMet() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS301")).thenReturn(testCourse);
        when(courseRepository.isPrerequisiteMet("S001", "CS301")).thenReturn(false);

        PrerequisiteNotMetException exception = assertThrows(
                PrerequisiteNotMetException.class,
                () -> enrollmentService.enrollCourse("S001", "CS301")
        );

        assertTrue(exception.getMessage().contains("Prerequisites not met"));
        verify(studentRepository, times(1)).findById("S001");
        verify(courseRepository, times(1)).findByCourseCode("CS301");
        verify(courseRepository, times(1)).isPrerequisiteMet("S001", "CS301");
        verify(courseRepository, never()).update(any(Course.class));
        verify(notificationService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    // ==================== enrollCourse() Edge Cases ====================

    @Test
    @DisplayName("MOCK - enrollCourse should handle course with zero enrolled students")
    void testEnrollCourse_EmptyCourse() {
        Course emptyCourse = new Course("CS401", "Advanced Topics", 4, 30, 0, "Dr. White");
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS401")).thenReturn(emptyCourse);
        when(courseRepository.isPrerequisiteMet("S001", "CS401")).thenReturn(true);

        Enrollment result = enrollmentService.enrollCourse("S001", "CS401");

        assertNotNull(result);
        verify(courseRepository).update(courseCaptor.capture());
        assertEquals(1, courseCaptor.getValue().getEnrolledCount());
    }

    @Test
    @DisplayName("MOCK - enrollCourse should handle multiple sequential enrollments")
    void testEnrollCourse_MultipleEnrollments() {
        Course course = new Course("CS501", "Machine Learning", 3, 35, 20, "Dr. Green");
        Student student1 = new Student("S004", "Alice Brown", "alice@email.com",
                "Data Science", 5, 3.8, "ACTIVE");
        Student student2 = new Student("S005", "Charlie Davis", "charlie@email.com",
                "AI Engineering", 4, 3.5, "ACTIVE");

        when(studentRepository.findById("S004")).thenReturn(student1);
        when(studentRepository.findById("S005")).thenReturn(student2);
        when(courseRepository.findByCourseCode("CS501")).thenReturn(course);
        when(courseRepository.isPrerequisiteMet(anyString(), eq("CS501"))).thenReturn(true);

        enrollmentService.enrollCourse("S004", "CS501");
        enrollmentService.enrollCourse("S005", "CS501");

        verify(courseRepository, times(2)).update(any(Course.class));
        verify(notificationService, times(2)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("MOCK - enrollCourse should set correct enrollment date")
    void testEnrollCourse_EnrollmentDate() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS301")).thenReturn(testCourse);
        when(courseRepository.isPrerequisiteMet("S001", "CS301")).thenReturn(true);

        Enrollment result = enrollmentService.enrollCourse("S001", "CS301");

        assertNotNull(result.getEnrollmentDate());
    }

    @Test
    @DisplayName("MOCK - enrollCourse should verify all method calls in correct order")
    void testEnrollCourse_VerifyCallOrder() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS301")).thenReturn(testCourse);
        when(courseRepository.isPrerequisiteMet("S001", "CS301")).thenReturn(true);

        enrollmentService.enrollCourse("S001", "CS301");

        var inOrder = inOrder(studentRepository, courseRepository, notificationService);
        inOrder.verify(studentRepository).findById("S001");
        inOrder.verify(courseRepository).findByCourseCode("CS301");
        inOrder.verify(courseRepository).isPrerequisiteMet("S001", "CS301");
        inOrder.verify(courseRepository).update(any(Course.class));
        inOrder.verify(notificationService).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("MOCK - enrollCourse should not update course when validation fails")
    void testEnrollCourse_NoUpdateOnValidationFailure() {
        Student suspendedStudent = new Student("S006", "Diana Evans", "diana@email.com",
                "Network Engineering", 6, 1.2, "SUSPENDED");
        when(studentRepository.findById("S006")).thenReturn(suspendedStudent);

        assertThrows(EnrollmentException.class,
                () -> enrollmentService.enrollCourse("S006", "CS301"));

        verify(courseRepository, never()).update(any(Course.class));
    }

    @Test
    @DisplayName("MOCK - enrollCourse should handle student with special characters in email")
    void testEnrollCourse_SpecialCharactersInEmail() {
        Student specialStudent = new Student("S007", "Frank O'Connor", "frank.o'connor+test@email.com",
                "Computer Science", 3, 3.5, "ACTIVE");
        when(studentRepository.findById("S007")).thenReturn(specialStudent);
        when(courseRepository.findByCourseCode("CS301")).thenReturn(testCourse);
        when(courseRepository.isPrerequisiteMet("S007", "CS301")).thenReturn(true);

        Enrollment result = enrollmentService.enrollCourse("S007", "CS301");

        assertNotNull(result);
        verify(notificationService).sendEmail(
                eq("frank.o'connor+test@email.com"),
                anyString(),
                anyString()
        );
    }

    @Test
    @DisplayName("MOCK - enrollCourse should handle course with maximum capacity")
    void testEnrollCourse_MaximumCapacity() {
        Course largeCourse = new Course("CS601", "Software Architecture", 4, 100, 50, "Dr. Black");
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS601")).thenReturn(largeCourse);
        when(courseRepository.isPrerequisiteMet("S001", "CS601")).thenReturn(true);

        Enrollment result = enrollmentService.enrollCourse("S001", "CS601");

        assertNotNull(result);
        verify(courseRepository).update(courseCaptor.capture());
        assertEquals(51, courseCaptor.getValue().getEnrolledCount());
    }

    @Test
    @DisplayName("MOCK - enrollCourse should verify notification content includes course name")
    void testEnrollCourse_NotificationContent() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS301")).thenReturn(testCourse);
        when(courseRepository.isPrerequisiteMet("S001", "CS301")).thenReturn(true);

        enrollmentService.enrollCourse("S001", "CS301");

        verify(notificationService).sendEmail(
                anyString(),
                anyString(),
                argThat(message -> message.contains("Algorithm Design"))
        );
    }

    // ==================== Additional Mock Tests ====================

    @Test
    @DisplayName("MOCK - validateCreditLimit with mocked GradeCalculator")
    void testValidateCreditLimit_WithMock() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(gradeCalculator.calculateMaxCredits(3.5)).thenReturn(24);

        boolean result = enrollmentService.validateCreditLimit("S001", 20);

        assertTrue(result);
        verify(studentRepository, times(1)).findById("S001");
        verify(gradeCalculator, times(1)).calculateMaxCredits(3.5);
    }

    @Test
    @DisplayName("MOCK - dropCourse with verification")
    void testDropCourse_WithMock() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS301")).thenReturn(testCourse);

        enrollmentService.dropCourse("S001", "CS301");

        verify(studentRepository, times(1)).findById("S001");
        verify(courseRepository, times(1)).findByCourseCode("CS301");
        verify(courseRepository, times(1)).update(any(Course.class));
        verify(notificationService, times(1)).sendEmail(
                eq("john@email.com"),
                eq("Course Drop Confirmation"),
                contains("Algorithm Design")
        );
    }

    // ==================== EXPLICIT EXCEPTION COVERAGE TESTS ====================

    @Test
    @DisplayName("EXPLICIT - StudentNotFoundException with try-catch")
    void testEnrollCourse_ExplicitStudentNotFound() {
        when(studentRepository.findById(anyString())).thenReturn(null);

        try {
            enrollmentService.enrollCourse("NULL_STUDENT", "CS999");
            fail("Should have thrown StudentNotFoundException");
        } catch (StudentNotFoundException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().contains("not found"));
        }

        verify(studentRepository).findById("NULL_STUDENT");
        verify(courseRepository, never()).findByCourseCode(anyString());
    }

    @Test
    @DisplayName("EXPLICIT - CourseNotFoundException with try-catch")
    void testEnrollCourse_ExplicitCourseNotFound() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode(anyString())).thenReturn(null);

        try {
            enrollmentService.enrollCourse("S001", "NULL_COURSE");
            fail("Should have thrown CourseNotFoundException");
        } catch (CourseNotFoundException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().contains("not found"));
        }

        verify(courseRepository).findByCourseCode("NULL_COURSE");
    }

    @Test
    @DisplayName("EXPLICIT - CourseFullException with try-catch")
    void testEnrollCourse_ExplicitCourseFull() {
        Course fullCourse = new Course("FULL", "Full Course", 3, 30, 30, "Dr. Full");
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("FULL")).thenReturn(fullCourse);

        try {
            enrollmentService.enrollCourse("S001", "FULL");
            fail("Should have thrown CourseFullException");
        } catch (CourseFullException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().contains("full"));
        }

        verify(courseRepository, never()).isPrerequisiteMet(anyString(), anyString());
    }

    @Test
    @DisplayName("EXPLICIT - PrerequisiteNotMetException with try-catch")
    void testEnrollCourse_ExplicitPrereqNotMet() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode("CS301")).thenReturn(testCourse);
        when(courseRepository.isPrerequisiteMet("S001", "CS301")).thenReturn(false);

        try {
            enrollmentService.enrollCourse("S001", "CS301");
            fail("Should have thrown PrerequisiteNotMetException");
        } catch (PrerequisiteNotMetException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().contains("not met"));
        }

        verify(courseRepository, never()).update(any());
    }

    @Test
    @DisplayName("EXPLICIT - EnrollmentException for suspended student with try-catch")
    void testEnrollCourse_ExplicitSuspended() {
        Student suspended = new Student("SUSP", "Suspended", "susp@test.com",
                "CS", 6, 1.0, "SUSPENDED");
        when(studentRepository.findById("SUSP")).thenReturn(suspended);

        try {
            enrollmentService.enrollCourse("SUSP", "CS301");
            fail("Should have thrown EnrollmentException");
        } catch (EnrollmentException e) {
            assertNotNull(e.getMessage());
            assertTrue(e.getMessage().toLowerCase().contains("suspend"));
        }

        verify(courseRepository, never()).findByCourseCode(anyString());
    }

    @Test
    @DisplayName("EXPLICIT - StudentNotFoundException in validateCreditLimit")
    void testValidateCreditLimit_ExplicitStudentNotFound() {
        when(studentRepository.findById(anyString())).thenReturn(null);

        try {
            enrollmentService.validateCreditLimit("NULL", 20);
            fail("Should throw StudentNotFoundException");
        } catch (StudentNotFoundException e) {
            assertNotNull(e.getMessage());
        }

        verify(gradeCalculator, never()).calculateMaxCredits(anyDouble());
    }

    @Test
    @DisplayName("EXPLICIT - StudentNotFoundException in dropCourse")
    void testDropCourse_ExplicitStudentNotFound() {
        when(studentRepository.findById(anyString())).thenReturn(null);

        try {
            enrollmentService.dropCourse("NULL", "CS101");
            fail("Should throw StudentNotFoundException");
        } catch (StudentNotFoundException e) {
            assertNotNull(e.getMessage());
        }

        verify(courseRepository, never()).findByCourseCode(anyString());
    }

    @Test
    @DisplayName("EXPLICIT - CourseNotFoundException in dropCourse")
    void testDropCourse_ExplicitCourseNotFound() {
        when(studentRepository.findById("S001")).thenReturn(testStudent);
        when(courseRepository.findByCourseCode(anyString())).thenReturn(null);

        try {
            enrollmentService.dropCourse("S001", "NULL_COURSE");
            fail("Should throw CourseNotFoundException");
        } catch (CourseNotFoundException e) {
            assertNotNull(e.getMessage());
        }

        verify(courseRepository, never()).update(any());
    }
}
