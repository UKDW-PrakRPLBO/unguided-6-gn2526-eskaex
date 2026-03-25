package org.rplbo.app;

import org.junit.jupiter.api.*;
import org.rplbo.app.Data.RekamMedis;
import org.rplbo.app.Data.User;
import org.rplbo.app.Manager.RekamMedisManager;
import org.rplbo.app.Manager.UserManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MentalAsylumTest {

    private UserManager userManager;
    private RekamMedisManager rekamMedisManager;
    private static int skorAkhir = 0;

    @BeforeEach
    public void setUp() {
        Connection testConn = DBConnectionManager.getConnection();
        userManager = new UserManager(testConn);
        rekamMedisManager = new RekamMedisManager(testConn);
    }

    @AfterEach
    public void tearDown() {
        // Membersihkan data testing dari kedua tabel
        String hapusUsers = "DELETE FROM users WHERE username LIKE 'test_%'";
        String hapusRekamMedis = "DELETE FROM rekam_medis WHERE nama_pasien LIKE 'test_%'";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement stmt1 = conn.prepareStatement(hapusUsers);
             PreparedStatement stmt2 = conn.prepareStatement(hapusRekamMedis)) {
            stmt1.executeUpdate();
            stmt2.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal membersihkan data test: " + e.getMessage());
        }
    }

    @AfterAll
    public static void cetakNilai() {
        System.out.println("\n=============================================");
        System.out.println("  TOTAL NILAI UTS MENTAL ASYLUM : " + skorAkhir + " / 100");
        System.out.println("=============================================\n");
    }

    // =========================================================
    // SKENARIO 1: FITUR PASIEN (Autentikasi & Lihat Dokter)
    // =========================================================
    @Test
    @Order(1)
    public void testFiturPasien() {
        // 1. Pasien Registrasi
        userManager.registerUser("test_pasienA", "pass123", "A@mail.com", "pasien");

        // 2. Verifikasi Login
        boolean loginSukses = userManager.authenticateUser("test_pasienA", "pass123");
        assertTrue(loginSukses, "Pasien harusnya bisa login dengan kredensial yang benar");

        // 3. Pasien Lihat Daftar Dokter
        userManager.registerUser("test_dokterX", "123", "docX@mail.com", "dokter");
        List<User> listDokter = userManager.getUsersByRole("dokter");
        assertFalse(listDokter.isEmpty(), "Pasien harusnya bisa melihat list yang berisi dokter");
        assertEquals("dokter", listDokter.get(0).getRole(), "Role yang ditarik haruslah dokter");

        skorAkhir += 25;
        System.out.println("[✔] Skenario 1 Lolos: Fitur Pasien (Auth & Lihat Dokter) (+25 Poin)");
    }

    // =========================================================
    // SKENARIO 2: FITUR GUEST (Cek Keberadaan Pasien)
    // =========================================================
    @Test
    @Order(2)
    public void testFiturGuest() {
        // Siapkan data rekam medis
        rekamMedisManager.tambahRekamMedis("test_dokterA", "test_pasienGuest", "Rahasia", "10-10-2026");

        // Guest Cek Keberadaan Pasien
        List<RekamMedis> hasilCari = rekamMedisManager.cariRekamMedisPasien("test_pasienGuest");
        assertFalse(hasilCari.isEmpty(), "Guest harusnya bisa menemukan status pasien yang dicari");

        skorAkhir += 25;
        System.out.println("[✔] Skenario 2 Lolos: Akses Publik Guest (Cek Pasien) (+25 Poin)");
    }

    // =========================================================
    // SKENARIO 3: FITUR DOKTER (Validasi, Insert, & Get All)
    // =========================================================
    @Test
    @Order(3)
    public void testFiturDokterInsertDanRead() {
        // 1. Dokter Validasi Role Pasien (Syarat sebelum Insert)
        userManager.registerUser("test_pasienAsli", "123", "pas@mail.com", "pasien");
        String cekRole = userManager.getUserRole("test_pasienAsli");
        assertEquals("pasien", cekRole, "Validasi harus mengenali bahwa ini adalah pasien");

        // 2. Dokter Tambah Rekam Medis
        boolean tambahSukses = rekamMedisManager.tambahRekamMedis("test_dokterB", "test_pasienAsli", "Sakit Kepala", "11-10-2026");
        assertTrue(tambahSukses, "Dokter harusnya berhasil menambah rekam medis");

        // 3. Dokter Lihat Semua Data (Get All)
        List<RekamMedis> allData = rekamMedisManager.getAllRekamMedis();
        assertFalse(allData.isEmpty(), "Dokter harusnya bisa menarik semua data rekam medis");

        skorAkhir += 25;
        System.out.println("[✔] Skenario 3 Lolos: Dokter Input & Read (+25 Poin)");
    }

    // =========================================================
    // SKENARIO 4: FITUR DOKTER (Update & Delete)
    // =========================================================
    @Test
    @Order(4)
    public void testFiturDokterEditDanHapus() {
        // Siapkan data dummy
        rekamMedisManager.tambahRekamMedis("test_dokterC", "test_pasienEdit", "Demam", "12-10-2026");
        List<RekamMedis> list = rekamMedisManager.cariRekamMedisPasien("test_pasienEdit");
        int targetId = list.get(0).getId();

        // 1. Dokter Edit Data (UPDATE)
        boolean hasilEdit = rekamMedisManager.editRekamMedis(targetId, "Sembuh Total");
        assertTrue(hasilEdit, "Dokter harusnya bisa mengedit diagnosis");

        // 2. Dokter Hapus Data (DELETE)
        boolean hasilHapus = rekamMedisManager.hapusRekamMedis(targetId);
        assertTrue(hasilHapus, "Dokter harusnya bisa menghapus data rekam medis");

        // Verifikasi terhapus
        List<RekamMedis> cekAkhir = rekamMedisManager.cariRekamMedisPasien("test_pasienEdit");
        assertTrue(cekAkhir.isEmpty(), "Data harusnya sudah bersih dari database");

        skorAkhir += 25;
        System.out.println("[✔] Skenario 4 Lolos: Dokter Update & Delete (+25 Poin)");
    }
}