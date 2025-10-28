package com.praktikum.model;

import java.time.LocalDateTime;

/**
 * Model class untuk data Enrollment (Pendaftaran Mata Kuliah)
 */
public class Enrollment {
    private String enrollmentId;
    private String studentId;
    private String courseCode;
    private LocalDateTime enrollmentDate;
    private String status; // PENDING, APPROVED, REJECTED

    public Enrollment() {
    }

    public Enrollment(String enrollmentId, String studentId, String courseCode,
                      LocalDateTime enrollmentDate, String status) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }

    // Getters and Setters
    public String getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
