import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class Perpustakaan {
    
    public static List<ItemPerpustakaan> bacaSemuaItemDariDatabase() {
        List<ItemPerpustakaan> items = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM perpustakaan_items")) {
    
            while (resultSet.next()) {
                String kodeItem = resultSet.getString("kode_item");
                String judul = resultSet.getString("judul");
                double hargaSewa = resultSet.getDouble("harga_sewa");
    
                // Ambil data dari buku_items (jika ada)
                try (PreparedStatement statementBuku = connection.prepareStatement(
                        "SELECT jumlah_halaman, tanggal_terbit FROM buku_items WHERE kode_item = ?")) {
                    statementBuku.setString(1, kodeItem);
                    ResultSet bukuResultSet = statementBuku.executeQuery();
    
                    if (bukuResultSet.next()) {
                        int jumlahHalaman = bukuResultSet.getInt("jumlah_halaman");
                        String tanggalTerbitString = bukuResultSet.getString("tanggal_terbit");
                        items.add(new Buku(kodeItem, judul, hargaSewa, jumlahHalaman, tanggalTerbitString));
                    }
                }
    
                // Ambil data dari cd_items (jika ada)
                try (PreparedStatement statementCD = connection.prepareStatement(
                        "SELECT durasi FROM cd_items WHERE kode_item = ?")) {
                    statementCD.setString(1, kodeItem);
                    ResultSet cdResultSet = statementCD.executeQuery();
    
                    if (cdResultSet.next()) {
                        int durasi = cdResultSet.getInt("durasi");
                        items.add(new CD(kodeItem, judul, hargaSewa, durasi));
                    }
                }
            }
    
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }
        return items;
    }
    

    public static void tambahItemKeDatabase(ItemPerpustakaan item) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            try (PreparedStatement statementPerpustakaanItems = connection.prepareStatement(
                    "INSERT INTO perpustakaan_items (kode_item, judul, harga_sewa) VALUES (?, ?, ?)")) {
    
                statementPerpustakaanItems.setString(1, item.getKodeItem());
                statementPerpustakaanItems.setString(2, item.getJudul());
                statementPerpustakaanItems.setDouble(3, item.getHargaSewa());
    
                statementPerpustakaanItems.executeUpdate();
            }
    
            if (item instanceof Buku) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO buku_items (kode_item, judul, harga_sewa, jumlah_halaman, tanggal_terbit) VALUES (?, ?, ?, ?, ?)")) {
    
                    statement.setString(1, item.getKodeItem());
                    statement.setString(2, item.getJudul());
                    statement.setDouble(3, item.getHargaSewa());
                    statement.setInt(4, ((Buku) item).getJumlahHalaman());
                    statement.setObject(5, ((Buku) item).getTanggalTerbit());
    
                    statement.executeUpdate();
                    System.out.println("Buku berhasil ditambahkan ke database.");
                }
            } else if (item instanceof CD) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO cd_items (kode_item, judul, harga_sewa, durasi) VALUES (?, ?, ?, ?)")) {
    
                    statement.setString(1, item.getKodeItem());
                    statement.setString(2, item.getJudul());
                    statement.setDouble(3, item.getHargaSewa());
                    statement.setInt(4, ((CD) item).getDurasi());
    
                    statement.executeUpdate();
                    System.out.println("CD berhasil ditambahkan ke database.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }
    }
    
    public static void tampilkanSemuaItem(List<ItemPerpustakaan> items) {
        System.out.println("Daftar Item Perpustakaan:");
    
        for (ItemPerpustakaan item : items) {
            if (item instanceof Buku) {
                Buku buku = (Buku) item;
                System.out.println("\nKode Item\t: " + buku.getKodeItem());
                System.out.println("Judul\t\t: " + buku.getJudul());
                System.out.println("Harga Sewa\t: " + buku.getHargaSewa());
                System.out.println("Jumlah Halaman\t: " + buku.getJumlahHalaman());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = dateFormat.format(buku.getTanggalTerbit());
                System.out.println("Tanggal Terbit\t: " + formattedDate);

            } else if (item instanceof CD) {
                CD cd = (CD) item;
                System.out.println("Kode Item\t: " + cd.getKodeItem());
                System.out.println("Judul\t\t: " + cd.getJudul());
                System.out.println("Harga Sewa\t: " + cd.getHargaSewa());
                System.out.println("Durasi\t\t: " + cd.getDurasi());
                
            }
    
            System.out.println(); // Add a line break between items
        }
    }    
    

    public static void updateItemDiDatabase(ItemPerpustakaan item) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            // Update perpustakaan_items
            try (PreparedStatement statementPerpustakaanItems = connection.prepareStatement(
                    "UPDATE perpustakaan_items SET judul = ?, harga_sewa = ? WHERE kode_item = ?")) {
    
                statementPerpustakaanItems.setString(1, item.getJudul());
                statementPerpustakaanItems.setDouble(2, item.getHargaSewa());
                statementPerpustakaanItems.setString(3, item.getKodeItem());
    
                int rowsUpdatedPerpustakaanItems = statementPerpustakaanItems.executeUpdate();
    
                if (rowsUpdatedPerpustakaanItems > 0) {
                    System.out.println("Informasi item di perpustakaan berhasil diperbarui.");
                } else {
                    System.out.println("Tidak ada item dengan kode " + item.getKodeItem() + " di perpustakaan.");
                }
            }
    
            if (item instanceof Buku) {
                // Update buku_items
                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE buku_items SET judul = ?, harga_sewa = ?, jumlah_halaman = ?, tanggal_terbit = ? WHERE kode_item = ?")) {
    
                    statement.setString(1, item.getJudul());
                    statement.setDouble(2, item.getHargaSewa());
                    statement.setInt(3, ((Buku) item).getJumlahHalaman());
                    statement.setObject(4, ((Buku) item).getTanggalTerbit());
                    statement.setString(5, item.getKodeItem());
    
                    int rowsUpdated = statement.executeUpdate();
    
                    if (rowsUpdated > 0) {
                        System.out.println("Informasi buku berhasil diperbarui.");
                    } else {
                        System.out.println("Tidak ada buku dengan kode " + item.getKodeItem());
                    }
                }
            } else if (item instanceof CD) {
                // Update cd_items
                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE cd_items SET judul = ?, harga_sewa = ?, durasi = ? WHERE kode_item = ?")) {
    
                    statement.setString(1, item.getJudul());
                    statement.setDouble(2, item.getHargaSewa());
                    statement.setInt(3, ((CD) item).getDurasi());
                    statement.setString(4, item.getKodeItem());
    
                    int rowsUpdated = statement.executeUpdate();
    
                    if (rowsUpdated > 0) {
                        System.out.println("Informasi CD berhasil diperbarui.");
                    } else {
                        System.out.println("Tidak ada CD dengan kode " + item.getKodeItem());
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }
    }
    
    
    public static void hapusItemDariDatabase(String kodeItem) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            // Delete from buku_items
            try (PreparedStatement statementBuku = connection.prepareStatement(
                    "DELETE FROM buku_items WHERE kode_item = ?")) {
    
                statementBuku.setString(1, kodeItem);
    
                int rowsDeletedBuku = statementBuku.executeUpdate();
    
                if (rowsDeletedBuku > 0) {
                    System.out.println("Buku berhasil dihapus.");
                } else {
                    System.out.println("Tidak ada buku dengan kode " + kodeItem);
                }
            }
    
            // Delete from cd_items
            try (PreparedStatement statementCD = connection.prepareStatement(
                    "DELETE FROM cd_items WHERE kode_item = ?")) {
    
                statementCD.setString(1, kodeItem);
    
                int rowsDeletedCD = statementCD.executeUpdate();
    
                if (rowsDeletedCD > 0) {
                    System.out.println("CD berhasil dihapus.");
                } else {
                    System.out.println("Tidak ada CD dengan kode " + kodeItem);
                }
            }
    
            // Delete from perpustakaan_items
            try (PreparedStatement statementPerpustakaanItems = connection.prepareStatement(
                    "DELETE FROM perpustakaan_items WHERE kode_item = ?")) {
    
                statementPerpustakaanItems.setString(1, kodeItem);
    
                int rowsDeletedPerpustakaanItems = statementPerpustakaanItems.executeUpdate();
    
                if (rowsDeletedPerpustakaanItems > 0) {
                    System.out.println("Item dihapus dari perpustakaan.");
                } else {
                    System.out.println("Tidak ada item dengan kode " + kodeItem + " di perpustakaan.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }
    }
    
}