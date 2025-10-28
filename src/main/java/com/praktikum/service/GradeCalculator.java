package com.praktikum.service;

import com.praktikum.model.CourseGrade;
import java.util.List;

/**
 * Class untuk perhitungan IPK dan status akademik mahasiswa
 * Class ini akan diuji dengan UNIT TESTING BIASA (tanpa mock/stub)
 */
public class GradeCalculator {

    /**
     * Menghitung IPK (Indeks Prestasi Kumulatif) mahasiswa
     * Formula: Total (Grade Point × SKS) / Total SKS
     *
     * @param grades List of CourseGrade yang berisi nilai mata kuliah
     * @return IPK dengan pembulatan 2 desimal
     * @throws IllegalArgumentException jika grade point invalid (< 0 atau > 4.0)
     */
    public double calculateGPA(List<CourseGrade> grades) {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }

        double totalPoints = 0.0;
        int totalCredits = 0;

        for (CourseGrade grade : grades) {
            if (grade.getGradePoint() < 0 || grade.getGradePoint() > 4.0) {
                throw new IllegalArgumentException("Invalid grade point: " + grade.getGradePoint());
            }
            totalPoints += grade.getGradePoint() * grade.getCredits();
            totalCredits += grade.getCredits();
        }

        if (totalCredits == 0) {
            return 0.0;
        }

        // Pembulatan ke 2 desimal
        return Math.round((totalPoints / totalCredits) * 100.0) / 100.0;
    }

    /**
     * Menentukan status akademik mahasiswa berdasarkan IPK dan semester
     *
     * Aturan:
     * - Semester 1-2: IPK >= 2.0 → ACTIVE, IPK < 2.0 → PROBATION
     * - Semester 3-4: IPK >= 2.25 → ACTIVE, IPK 2.0-2.24 → PROBATION, IPK < 2.0 → SUSPENDED
     * - Semester 5+: IPK >= 2.5 → ACTIVE, IPK 2.0-2.49 → PROBATION, IPK < 2.0 → SUSPENDED
     *
     * @param gpa IPK mahasiswa (0.0 - 4.0)
     * @param semester Semester mahasiswa (harus > 0)
     * @return Status akademik: ACTIVE, PROBATION, atau SUSPENDED
     * @throws IllegalArgumentException jika gpa atau semester invalid
     */
    public String determineAcademicStatus(double gpa, int semester) {
        if (gpa < 0 || gpa > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0 and 4.0");
        }
        if (semester < 1) {
            throw new IllegalArgumentException("Semester must be positive");
        }

        // Semester 1-2: minimal IPK 2.0
        if (semester <= 2) {
            return gpa >= 2.0 ? "ACTIVE" : "PROBATION";
        }

        // Semester 3-4: minimal IPK 2.25
        if (semester <= 4) {
            if (gpa >= 2.25) return "ACTIVE";
            if (gpa >= 2.0) return "PROBATION";
            return "SUSPENDED";
        }

        // Semester 5+: minimal IPK 2.5
        if (gpa >= 2.5) return "ACTIVE";
        if (gpa >= 2.0) return "PROBATION";
        return "SUSPENDED";
    }

    /**
     * Menghitung jumlah SKS maksimal yang boleh diambil mahasiswa
     * berdasarkan IPK
     *
     * Aturan:
     * - IPK >= 3.0: maksimal 24 SKS
     * - IPK 2.5-2.99: maksimal 21 SKS
     * - IPK 2.0-2.49: maksimal 18 SKS
     * - IPK < 2.0: maksimal 15 SKS
     *
     * @param gpa IPK mahasiswa (0.0 - 4.0)
     * @return Jumlah SKS maksimal yang boleh diambil
     * @throws IllegalArgumentException jika gpa invalid
     */
    public int calculateMaxCredits(double gpa) {
        if (gpa < 0 || gpa > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0 and 4.0");
        }

        if (gpa >= 3.0) return 24;
        if (gpa >= 2.5) return 21;
        if (gpa >= 2.0) return 18;
        return 15;
    }
}
