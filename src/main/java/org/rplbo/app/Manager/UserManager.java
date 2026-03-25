package org.rplbo.app.Manager;
import org.rplbo.app.DBConnectionManager;
import org.rplbo.app.Data.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * ========================================================================
 *              PETUNJUK PENGERJAAN (BERLAKU UNTUK SETIAP METHOD)
 * ========================================================================
 *              TODO : LENGKAPILAH SETIAP METHOD YANG KOSONG
 * TODO 1: Siapkan Query SQL.
 * TODO 2: Buka koneksi database dan siapkan PreparedStatement. Gunakan prepareStatement(query)
 * TODO 3: Lakukan parameter binding (isi nilai tanda '?'). Gunakan setString() / setInt()
 * TODO 4: Eksekusi query ke dalam database. Gunakan executeQuery()
 * TODO 5: Evaluasi dan olah hasil eksekusi. Gunakan next()
 * TODO 6: Tangkap exception (SQLException) jika terjadi kegagalan database. (try catch (SQLException e))
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

public class UserManager {
    private Connection connection;

    // Constructor untuk menerima koneksi dari Main / DBConfig
    public UserManager(Connection connection) {
        this.connection = connection;
    }

    // Constructor kosong (opsional, tapi jika dipakai pastikan kamu nge-set koneksinya nanti)
    public UserManager() {
    }

    // TODO LENGKAPILAH SETIAP METHOD YANG KOSONG DIBAWAH INI
    // --- METHOD CARI USER SESUAI ROLE ---
    public List<User> getUsersByRole(String role) {
        List<User> userList = new ArrayList<>();
        return userList;
    }

    // --- METHOD REGISTER (INSERT) ---
    public boolean registerUser(String username, String password, String email, String role) {
        return false;
    }

    // --- METHOD LOGIN (SELECT) ---
    public boolean authenticateUser(String username, String password) {
        return false;
    }

    // --- METHOD CARI ROLE USER ---
    public String getUserRole(String username) {
        // TODO 1: Siapkan Query SQL.
        String query = "SELECT role FROM users WHERE username = ?";

        // TODO 2: Buka koneksi database dan siapkan PreparedStatement.
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            // TODO 3: Lakukan parameter binding (isi nilai tanda '?').
            stmt.setString(1, username);

            // TODO 4: Eksekusi query ke dalam database.
            try (ResultSet rs = stmt.executeQuery()) {

                // TODO 5: Evaluasi dan olah hasil eksekusi.
                if (rs.next()) {
                    return rs.getString("role"); // Ambil role-nya
                }
            }
            // TODO 6: Tangkap exception (SQLException) jika terjadi kegagalan database.
        } catch (SQLException e) {
            System.err.println("Error ambil role: " + e.getMessage());
        }
        return "guest"; // Default jika terjadi error
    }
}
