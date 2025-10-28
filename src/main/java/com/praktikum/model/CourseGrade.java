package com.praktikum.model;

/**
 * Helper class untuk menyimpan data nilai mata kuliah
 * Digunakan untuk perhitungan IPK
 */
public class CourseGrade {
    private String courseCode;
    private int credits;
    private double gradePoint; // 0.0 - 4.0 (A=4.0, B=3.0, C=2.0, D=1.0, E=0.0)

    public CourseGrade() {
    }

    public CourseGrade(String courseCode, int credits, double gradePoint) {
        this.courseCode = courseCode;
        this.credits = credits;
        this.gradePoint = gradePoint;
    }

    // Getters and Setters
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(double gradePoint) {
        this.gradePoint = gradePoint;
    }
}
