package com.praktikum.service;

/**
 * Interface untuk service notifikasi
 * Interface ini akan di-mock dalam unit testing
 */
public interface NotificationService {
    /**
     * Mengirim email ke mahasiswa
     * @param email Alamat email tujuan
     * @param subject Subject email
     * @param message Isi pesan email
     */
    void sendEmail(String email, String subject, String message);

    /**
     * Mengirim SMS ke mahasiswa
     * @param phone Nomor telepon tujuan
     * @param message Isi pesan SMS
     */
    void sendSMS(String phone, String message);
}
