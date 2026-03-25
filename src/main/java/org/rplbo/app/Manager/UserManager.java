package org.rplbo.app.Manager;
import org.rplbo.app.DBConnectionManager;
import org.rplbo.app.Data.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private Connection connection;

    // Constructor untuk menerima koneksi dari Main / DBConfig
    public UserManager(Connection connection) {
        this.connection = connection;
    }

    // Constructor kosong (opsional, tapi jika dipakai pastikan kamu nge-set koneksinya nanti)
    public UserManager() {
    }

    public List<User> getUsersByRole(String role) {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Bungkus ke dalam objek User!
                    User u = new User(
                            rs.getString("username"),
                            rs.getString("role"),
                            rs.getString("password"),
                            rs.getString("email")
                    );
                    userList.add(u);
                }
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil daftar " + role + ": " + e.getMessage());
        }

        return userList;
    }

    // --- FITUR REGISTER (INSERT) ---
    public boolean registerUser(String username, String password, String email, String role) {
        String query = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, role);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Akan return true jika ada baris yang berhasil ditambahkan

        } catch (SQLException e) {
            System.err.println("Error saat registrasi: " + e.getMessage());
            return false;
        }
    }

    // --- FITUR LOGIN (SELECT) ---
    public boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Eksekusi query dan tampung hasilnya di ResultSet
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Jika rs.next() bernilai true, berarti kombinasi user & pass ditemukan
            }

        } catch (SQLException e) {
            System.err.println("Error saat autentikasi: " + e.getMessage());
            return false;
        }
    }

    public String getUserRole(String username) {
        String query = "SELECT role FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role"); // Ambil role-nya
                }
            }
        } catch (SQLException e) {
            System.err.println("Error ambil role: " + e.getMessage());
        }
        return "guest"; // Default jika terjadi error
    }

    // --- UPDATE PROFILE (Edit Password & Email Pasien/Dokter) ---
    public boolean updateUserProfile(String oldUsername, String newUsername, String newPassword, String newEmail) {

        // TODO 1: Siapkan Query UPDATE ke tabel 'users'.
        // Tambahkan kolom 'username' di bagian SET.
        String query = "UPDATE users SET username = ?, password = ?, email = ? WHERE username = ?";

        // TODO 2: Buka koneksi dan siapkan PreparedStatement
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            // TODO 3: Binding data (isi tanda ? sesuai urutan di query)
            stmt.setString(1, newUsername); // Kolom username baru
            stmt.setString(2, newPassword);
            stmt.setString(3, newEmail);
            stmt.setString(4, oldUsername); // Klausa WHERE (mencari data lama)

            // TODO 4: Eksekusi query
            int rowsAffected = stmt.executeUpdate();

            // TODO 5: Kembalikan true jika berhasil
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error saat update profil: " + e.getMessage());
            return false;
        }
    }

    /*
     * ========================================================================
     * PETUNJUK PENGERJAAN (BERLAKU UNTUK SETIAP METHOD)
     * ========================================================================
     * TODO 1: Siapkan Query SQL.
     * TODO 2: Buka koneksi database dan siapkan PreparedStatement.
     * TODO 3: Lakukan parameter binding (isi nilai tanda '?').
     * TODO 4: Eksekusi query ke dalam database.
     * TODO 5: Evaluasi dan olah hasil eksekusi.
     * TODO 6: Tangkap exception (SQLException) jika terjadi kegagalan database.
     *
     * HINT ATURAN EMAS:
     * - Gunakan stmt.executeUpdate() jika merubah isi tabel (INSERT, UPDATE, DELETE).
     * - Gunakan stmt.executeQuery() jika hanya membaca tabel (SELECT).
     * - Untuk INSERT/UPDATE/DELETE : Cek apakah baris yang terpengaruh (rowsAffected) > 0, lalu return true/false.
     * - Untuk SELECT : Gunakan perulangan while(rs.next()) untuk mengekstrak data dan membungkusnya ke dalam List/Objek.
     *
     * Tampilkan pesan error di konsol (System.err.println) agar mudah di-debug,
     * lalu kembalikan nilai default (misal: return false, atau return List kosong).
     * ========================================================================
     */
}