package com.praktikum   .model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class untuk data Mata Kuliah
 */
public class Course {
    private String courseCode;
    private String courseName;
    private int credits;
    private int capacity;
    private int enrolledCount;
    private String lecturer;
    private List<String> prerequisites; // Mata kuliah prasyarat

    public Course() {
        this.prerequisites = new ArrayList<>();
    }

    public Course(String courseCode, String courseName, int credits,
                  int capacity, int enrolledCount, String lecturer) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.capacity = capacity;
        this.enrolledCount = enrolledCount;
        this.lecturer = lecturer;
        this.prerequisites = new ArrayList<>();
    }

    // Getters and Setters
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getEnrolledCount() {
        return enrolledCount;
    }

    public void setEnrolledCount(int enrolledCount) {
        this.enrolledCount = enrolledCount;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

    public void addPrerequisite(String courseCode) {
        this.prerequisites.add(courseCode);
    }
}
