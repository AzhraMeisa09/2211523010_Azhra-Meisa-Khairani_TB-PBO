// Kelas untuk menghubungkan ke database
import java.sql.*;

public class DatabaseConnector {
    // Detail koneksi database
    public static final String DB_URL = "jdbc:mysql://localhost:3306/perpustakaan";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "";

    // Metode untuk mendapatkan koneksi database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
