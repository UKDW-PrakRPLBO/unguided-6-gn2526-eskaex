![alt text](AsylumDiagram.png?raw=true)
# Sistem Manajemen Asylum (Tugas Praktikum JDBC - UG6)
AsylumSystem adalah aplikasi berbasis Java Console yang digunakan untuk mengelola data rekam medis pasien. Sistem ini memiliki fitur autentikasi pengguna, manajemen rekam medis, serta *role-based access* antara dokter, pasien, dan publik (guest).

Aplikasi ini menggunakan Java OOP, JDBC, dan Database SQLite untuk menyimpan serta mengelola data.

## 1. Struktur Arsitektur & Direktori
* `User.java` : Class Model (POJO) yang merepresentasikan data entitas pengguna (Dokter dan Pasien).
* `RekamMedis.java` : Class Model (POJO) yang merepresentasikan detail informasi satu riwayat rekam medis.

* `UserManager.java` : Mengurus operasi *query* database untuk autentikasi (Login), registrasi, pencarian dokter, dan pembaruan profil pengguna.
* `RekamMedisManager.java` : Mengurus operasi CRUD khusus untuk pencatatan dan pencarian rekam medis pasien.

* `DBConnectionManager.java` : Kelas *Utility* untuk mengatur siklus hidup koneksi database (`getConnection()`).
* `AsylumSystem.java` : *Main Class* (Entry Point) yang menjalankan sistem antarmuka menu, *Scanner*, dan navigasi aplikasi.
* `MentalAsylumTest.java` : Kelas *Unit Testing* terintegrasi menggunakan framework **JUnit 5**.

## 2. Skema Basis Data (SQLite)
Tabel `users` (Menyimpan kredensial dan hak akses)
* `id` (INTEGER, Primary Key, Auto-Increment)
* `username` (TEXT, Unique)
* `email` (TEXT)
* `password` (TEXT)
* `role` (TEXT) -> Berisi "dokter" atau "pasien"

Tabel `rekam_medis` (Menyimpan riwayat diagnosis)
* `id` (INTEGER, Primary Key, Auto-Increment)
* `nama_pasien` (TEXT)
* `nama_dokter` (TEXT)
* `diagnosis` (TEXT)
* `tanggal` (TEXT)

## 3. Fitur Sistem Berdasarkan Role (Hak Akses)
**A. Role Dokter (Tenaga Medis)**
1. **Tambah Rekam Medis:** Menginput diagnosis baru dengan validasi status pasien (`CREATE`).
2. **Registrasi:** Mendaftarkan akun pasien/dokter baru secara langsung (`INSERT`).
3. **Edit Rekam Medis:** Mengubah teks diagnosis pasien (`UPDATE`).
4. **Hapus Rekam Medis:** Menghapus data riwayat medis (`DELETE`).
5. **Lihat & Cari Pasien:** Menarik seluruh data atau mencari spesifik rekam medis (`READ`).

**B. Role Pasien (Akses Terbatas)**
1. **Lihat Riwayat Sendiri:** Hanya dapat melihat rekam medis yang namanya cocok dengan akunnya demi privasi.
2. **Lihat Daftar Dokter:** Menampilkan kontak dokter yang tersedia.

**C. Role Guest (Akses Publik / Tanpa Login)**
1. **Cek Keberadaan Pasien:** Melakukan pencarian untuk mengetahui status rawat pasien, dengan menyembunyikan detail diagnosis penyakitnya.

## 4. Pengujian Sistem (Unit Testing)
**Skenario Pengujian (Total 100 Poin):**
1. `testFiturPasien` (25 Poin) -> Menguji alur entitas Pasien (Registrasi, *Update* Profil, dan verifikasi Login dengan data baru).
2. `testFiturGuest` (25 Poin) -> Menguji akses publik/Guest (Melihat daftar Dokter dan mengecek status keberadaan Pasien).
3. `testFiturDokterInsertDanRead` (25 Poin) -> Menguji hak akses Dokter (Validasi *role*, *Insert* rekam medis, dan menarik seluruh riwayat medis).
4. `testFiturDokterEditDanHapus` (25 Poin) -> Menguji modifikasi data oleh Dokter (*Update* teks diagnosis dan *Delete* data rekam medis).

**Cara Menjalankan:**
1. Jalankan (*Run*) file `AsylumSystem.java` untuk memulai aplikasi berbasis interaksi *Scanner*.
2. Jalankan (*Run*) file `MentalAsylumTest.java` untuk memulai penilaian sistem secara otomatis.
---

**Cara Mengerjakan:**

Tugas Anda adalah melengkapi logika Database Connectivity (JDBC) pada kelas-kelas Manager yang telah disediakan. Pastikan Anda menggunakan blok try-with-resources dan mencegah SQL Injection.

1. Class UserManager
   - Lengkapilah Method Login (authenticateUser)
   - Lengkapilah Method Registrasi (registerUser)
   - Lengkapilah Method Mencari Users Sesuai Role (getUserByRole)
2. Class RekamMedisManager
   - Lengkapilah Method Tambah (tambahRekamMedis) — CREATE
   - Lengkapilah Method Cari (cariRekamMedisPasien dan getAllRekamMedis) — READ
   - Lengkapilah Method Edit (editRekamMedis) — UPDATE
   - Lengkapilah Method Hapus (hapusRekamMedisPasien) — DELETE
3. Class DBConnectionManager
   - Lengkapilah Class DBConnectionManager sehingga koneksi ke database SQLite dapat tersambung dengan baik dan dapat digunakan oleh seluruh Manager.
---

![alt text](LoginBerhasil.png?raw=true)
![alt text](LoginGagal.png?raw=true)

> [!CAUTION]
> # 🛑 PERHATIAN
> ### HANYA BOLEH MENGUBAH CLASS MANAGERS (USERMANAGER, REKAMMEDISMANAGER DAN DBCONNECTIONMANAGER)
