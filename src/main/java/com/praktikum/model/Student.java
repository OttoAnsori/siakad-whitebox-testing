package com.praktikum.model;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private String major;
    private int semester;
    private double gpa; // IPK
    private String academicStatus; // ACTIVE, PROBATION, SUSPENDED

    public Student() {
    }

    public Student(String studentId, String name, String email, String major,
                   int semester, double gpa, String academicStatus) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.major = major;
        this.semester = semester;
        this.gpa = gpa;
        this.academicStatus = academicStatus;
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }
}
