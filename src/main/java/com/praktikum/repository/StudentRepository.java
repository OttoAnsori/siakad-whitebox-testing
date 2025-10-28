package com.praktikum.repository;

import com.praktikum.model.Course;
import com.praktikum.model.Student;
import java.util.List;

/**
 * Interface untuk akses data mahasiswa
 * Interface ini akan di-stub atau di-mock dalam unit testing
 */
public interface StudentRepository {
    /**
     * Mencari mahasiswa berdasarkan student ID
     * @param studentId ID mahasiswa
     * @return Student object atau null jika tidak ditemukan
     */
    Student findById(String studentId);

    /**
     * Update data mahasiswa
     * @param student Student object yang akan diupdate
     */
    void update(Student student);

    /**
     * Mendapatkan daftar mata kuliah yang sudah diselesaikan mahasiswa
     * @param studentId ID mahasiswa
     * @return List of Course yang sudah diselesaikan
     */
    List<Course> getCompletedCourses(String studentId);
}
