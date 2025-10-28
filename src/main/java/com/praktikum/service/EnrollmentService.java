package com.praktikum.service;

import com.praktikum.exception.*;
import com.praktikum.model.Course;
import com.praktikum.model.Enrollment;
import com.praktikum.model.Student;
import com.praktikum.repository.CourseRepository;
import com.praktikum.repository.StudentRepository;
import java.time.LocalDateTime;

/**
 * Service untuk mengelola enrollment (pendaftaran mata kuliah)
 * Class ini akan diuji dengan STUB dan MOCK
 */
public class EnrollmentService {
    private StudentRepository studentRepository;
    private CourseRepository courseRepository;
    private NotificationService notificationService;
    private GradeCalculator gradeCalculator;

    public EnrollmentService(StudentRepository studentRepository,
                             CourseRepository courseRepository,
                             NotificationService notificationService,
                             GradeCalculator gradeCalculator) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.notificationService = notificationService;
        this.gradeCalculator = gradeCalculator;
    }

    /**
     * Mendaftarkan mahasiswa ke mata kuliah
     * Method ini akan diuji dengan MOCK
     *
     * @param studentId ID mahasiswa
     * @param courseCode Kode mata kuliah
     * @return Enrollment object jika berhasil
     * @throws StudentNotFoundException jika mahasiswa tidak ditemukan
     * @throws EnrollmentException jika mahasiswa di-suspend
     * @throws CourseNotFoundException jika mata kuliah tidak ditemukan
     * @throws CourseFullException jika mata kuliah sudah penuh
     * @throws PrerequisiteNotMetException jika prasyarat tidak terpenuhi
     */
    public Enrollment enrollCourse(String studentId, String courseCode) {
        // Validate student
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            throw new StudentNotFoundException("Student not found: " + studentId);
        }

        // Check academic status
        if ("SUSPENDED".equals(student.getAcademicStatus())) {
            throw new EnrollmentException("Student is suspended");
        }

        // Validate course
        Course course = courseRepository.findByCourseCode(courseCode);
        if (course == null) {
            throw new CourseNotFoundException("Course not found: " + courseCode);
        }

        // Check capacity
        if (course.getEnrolledCount() >= course.getCapacity()) {
            throw new CourseFullException("Course is full");
        }

        // Check prerequisites
        if (!courseRepository.isPrerequisiteMet(studentId, courseCode)) {
            throw new PrerequisiteNotMetException("Prerequisites not met");
        }

        // Create enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(generateEnrollmentId());
        enrollment.setStudentId(studentId);
        enrollment.setCourseCode(courseCode);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setStatus("APPROVED");

        // Update course enrollment count
        course.setEnrolledCount(course.getEnrolledCount() + 1);
        courseRepository.update(course);

        // Send notification
        notificationService.sendEmail(student.getEmail(),
                "Enrollment Confirmation",
                "You have been enrolled in: " + course.getCourseName());

        return enrollment;
    }

    /**
     * Validasi batas SKS yang boleh diambil mahasiswa
     * Method ini akan diuji dengan STUB
     *
     * @param studentId ID mahasiswa
     * @param requestedCredits Jumlah SKS yang diminta
     * @return true jika SKS masih dalam batas, false jika melebihi
     * @throws StudentNotFoundException jika mahasiswa tidak ditemukan
     */
    public boolean validateCreditLimit(String studentId, int requestedCredits) {
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            throw new StudentNotFoundException("Student not found");
        }

        int maxCredits = gradeCalculator.calculateMaxCredits(student.getGpa());
        return requestedCredits <= maxCredits;
    }

    /**
     * Drop (membatalkan) mata kuliah yang sudah didaftarkan
     * Method ini akan diuji dengan STUB
     *
     * @param studentId ID mahasiswa
     * @param courseCode Kode mata kuliah
     * @throws StudentNotFoundException jika mahasiswa tidak ditemukan
     * @throws CourseNotFoundException jika mata kuliah tidak ditemukan
     */
    public void dropCourse(String studentId, String courseCode) {
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            throw new StudentNotFoundException("Student not found");
        }

        Course course = courseRepository.findByCourseCode(courseCode);
        if (course == null) {
            throw new CourseNotFoundException("Course not found");
        }

        // Update enrollment count
        course.setEnrolledCount(course.getEnrolledCount() - 1);
        courseRepository.update(course);

        // Send notification
        notificationService.sendEmail(student.getEmail(),
                "Course Drop Confirmation",
                "You have dropped: " + course.getCourseName());
    }

    /**
     * Generate unique enrollment ID
     * @return Enrollment ID
     */
    private String generateEnrollmentId() {
        return "ENR-" + System.currentTimeMillis();
    }
}
