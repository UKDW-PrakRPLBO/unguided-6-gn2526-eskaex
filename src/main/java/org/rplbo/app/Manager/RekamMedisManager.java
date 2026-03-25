package org.rplbo.app.Manager;

import org.rplbo.app.DBConnectionManager;
import org.rplbo.app.Data.RekamMedis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RekamMedisManager {
    private Connection connection;

    public RekamMedisManager(Connection connection) {
        this.connection = connection;
    }

    // --- 1. CREATE (Tambah Rekam Medis) ---
    public boolean tambahRekamMedis(String namaDokter,String namaPasien, String diagnosis, String tanggal) {
        String query = "INSERT INTO rekam_medis (nama_dokter, nama_pasien, diagnosis, tanggal) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, namaDokter);
            stmt.setString(2, namaPasien);
            stmt.setString(3, diagnosis);
            stmt.setString(4, tanggal);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error saat tambah rekam medis: " + e.getMessage());
            return false;
        }
    }

    // --- 2. READ ALL ---
    public List<RekamMedis> getAllRekamMedis() {
        List<RekamMedis> rekamMedisList = new ArrayList<>();
        String sql = "SELECT * FROM rekam_medis";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // URUTAN YANG BENAR SESUAI CONSTRUCTOR
                RekamMedis rm = new RekamMedis(
                        rs.getInt("id"),
                        rs.getString("nama_dokter"),
                        rs.getString("nama_pasien"),
                        rs.getString("diagnosis"),
                        rs.getString("tanggal")
                );
                rekamMedisList.add(rm);
            }

        } catch (SQLException e) {
            System.err.println("Gagal membaca data rekam medis: " + e.getMessage());
        }
        return rekamMedisList;
    }

    // --- 3. UPDATE ---
    public boolean editRekamMedis(int idRekamMedis, String diagnosisBaru) {
        String query = "UPDATE rekam_medis SET diagnosis = ? WHERE id = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, diagnosisBaru);
            stmt.setInt(2, idRekamMedis);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error saat edit rekam medis: " + e.getMessage());
            return false;
        }
    }

    // --- 4. DELETE ---
    public boolean hapusRekamMedis(int idRekamMedis) {
        String query = "DELETE FROM rekam_medis WHERE id = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idRekamMedis);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error saat hapus rekam medis: " + e.getMessage());
            return false;
        }
    }

    // --- 5. READ ---
    public List<RekamMedis> cariRekamMedisPasien(String nama) {
        List<RekamMedis> resultList = new ArrayList<>();
        String sql = "SELECT * FROM rekam_medis WHERE nama_pasien LIKE ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nama + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RekamMedis rm = new RekamMedis(
                            rs.getInt("id"),
                            rs.getString("nama_dokter"),
                            rs.getString("nama_pasien"),
                            rs.getString("diagnosis"),
                            rs.getString("tanggal")
                    );
                    resultList.add(rm);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mencari data rekam medis: " + e.getMessage());
        }
        return resultList;
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