import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean isAuthenticated = false;

        do {
            System.out.println("\n========================================");
            System.out.print("Admin Username: ");
            String adminUsername = scanner.nextLine();
            System.out.print("Admin Password: ");
            String adminPassword = scanner.nextLine();
            System.out.println("========================================");

            isAuthenticated = authenticateAdmin(adminUsername, adminPassword);

            if (isAuthenticated) {
                runProgram(scanner);
            } else {
                System.out.println("Invalid admin credentials. Please try again.");
            }

        } while (!isAuthenticated);

        scanner.close();
    }

    private static boolean authenticateAdmin(String username, String password) {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }

    public static void runProgram(Scanner scanner) {
        List<ItemPerpustakaan> perpustakaanItems = Perpustakaan.bacaSemuaItemDariDatabase();

        int pilihan;
        do {
            System.out.println("\nMenu Perpustakaan:");
            System.out.println("1. Tambah Buku");
            System.out.println("2. Tambah CD");
            System.out.println("3. Tampilkan Semua Item");
            System.out.println("4. Update Informasi Item");
            System.out.println("5. Hapus Item");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            try {
                pilihan = scanner.nextInt();
                scanner.nextLine(); 
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid. Masukkan angka.");
                scanner.nextLine(); 
                pilihan = -1;
            }

            switch (pilihan) {
                case 1:
                    // Book input section
                    tambahBuku(scanner, perpustakaanItems);
                    break;

                case 2:
                    // CD input section
                    tambahCD(scanner, perpustakaanItems);
                    break;

                case 3:
                    // Display all items section
                    tampilkanSemuaItem(perpustakaanItems);
                    break;

                case 4:
                    // Update item section
                    updateInformasiItem(scanner, perpustakaanItems);
                    break;

                case 5:
                    // Delete item section
                    hapusItem(scanner, perpustakaanItems);
                    break;

                case 0:
                    System.out.println("Keluar dari program.");
                    break;

                default:
                    System.out.println("Pilihan tidak valid.");
                    break;
            }

        } while (pilihan != 0);
    }

    // Metode tambahBuku 
    private static void tambahBuku(Scanner scanner, List<ItemPerpustakaan> perpustakaanItems) {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("           INPUT BOOK IN LIBRARY         ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        try {
            System.out.println("Masukkan informasi Buku");
            System.out.print("Kode Buku\t: ");
            String kodeBuku = scanner.next();
            System.out.print("Judul Buku\t: ");
            String judulBuku = scanner.next();
            System.out.print("Harga Sewa\t: ");
            double hargaSewaBuku = scanner.nextDouble();
            System.out.print("Jumlah Halaman\t: ");
            int jumlahHalamanBuku = scanner.nextInt();
            System.out.print("Tanggal Terbit\t: ");
            String tanggalTerbitBuku = scanner.next();

            Buku buku = new Buku(kodeBuku, judulBuku, hargaSewaBuku, jumlahHalamanBuku, tanggalTerbitBuku);
            Perpustakaan.tambahItemKeDatabase(buku);
            perpustakaanItems.add(buku);
            System.out.println("=======================================");
        } catch (InputMismatchException e) {
            System.out.println("Input tidak valid. Pastikan format input sesuai.");
            scanner.nextLine(); 
        }
    }

    // Metode tambahCD 
    private static void tambahCD(Scanner scanner, List<ItemPerpustakaan> perpustakaanItems) {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("            INPUT CD IN LIBRARY          ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        try {
            System.out.println("Masukkan informasi CD");
            System.out.print("Kode CD\t\t: ");
            String kodeCD = scanner.next();
            System.out.print("Judul CD\t: ");
            String judulCD = scanner.next();
            System.out.print("Harga Sewa\t: ");
            double hargaSewaCD = scanner.nextDouble();
            System.out.print("Durasi CD (menit): ");
            int durasiCD = scanner.nextInt();

            CD cd = new CD(kodeCD, judulCD, hargaSewaCD, durasiCD);
            Perpustakaan.tambahItemKeDatabase(cd);
            perpustakaanItems.add(cd);

            System.out.println("==========================================");
            // Setelah menambah CD, tambahkan kode untuk menampilkan biaya sewa dengan jumlah hari tertentu
            System.out.print("Jumlah Hari\t\t\t: ");
            int jumlahHari = scanner.nextInt();
            double biayaSewa = cd.hitungBiayaSewa(jumlahHari);
            System.out.println("Biaya Sewa CD untuk " + jumlahHari + " hari\t: " + biayaSewa);
            System.out.println("==========================================");
        } catch (InputMismatchException e) {
            System.out.println("Input tidak valid. Pastikan format input sesuai.");
            scanner.nextLine(); 
        }
    }

    // Metode tampilkanSemuaItem 
    private static void tampilkanSemuaItem(List<ItemPerpustakaan> perpustakaanItems) {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("            VIEW ITEM LIBRARY            ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Perpustakaan.tampilkanSemuaItem(perpustakaanItems);
        System.out.println("=======================================");
    }

    // Metode updateInformasiItem 
    private static void updateInformasiItem(Scanner scanner, List<ItemPerpustakaan> perpustakaanItems) {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("           UPDATE ITEM LIBRARY           ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Kode Item yang akan diperbarui: ");
        String kodeItemToUpdate = scanner.next();
        ItemPerpustakaan itemToUpdate = perpustakaanItems.stream()
                .filter(item -> item.getKodeItem().equals(kodeItemToUpdate))
                .findFirst()
                .orElse(null);

        if (itemToUpdate != null) {
            System.out.println("Masukkan Judul Baru: ");
            String newTitle = scanner.next();
            itemToUpdate.judul = newTitle;

            System.out.println("Masukkan Harga Sewa Baru: ");
            double newHarga = scanner.nextDouble();
            itemToUpdate.hargaSewa = newHarga;

            if (itemToUpdate instanceof Buku) {
                System.out.println("Masukkan Jumlah Halaman Baru: ");
                int newJumlahHalaman = scanner.nextInt();
                ((Buku) itemToUpdate).setJumlahHalaman(newJumlahHalaman);
            } else if (itemToUpdate instanceof CD) {
                System.out.println("Masukkan Durasi Baru (menit): ");
                int newDurasi = scanner.nextInt();
                ((CD) itemToUpdate).setDurasi(newDurasi);
            }

            Perpustakaan.updateItemDiDatabase(itemToUpdate);
        } else {
            System.out.println("Tidak ada item dengan kode " + kodeItemToUpdate);
        }
        System.out.println("=======================================");
    }

    // Metode hapusItem 
    private static void hapusItem(Scanner scanner, List<ItemPerpustakaan> perpustakaanItems) {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("           DELETE ITEM LIBRARY           ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Masukkan Kode Item yang akan dihapus: ");
        String kodeItemToDelete = scanner.next();
        Perpustakaan.hapusItemDariDatabase(kodeItemToDelete);
        perpustakaanItems.removeIf(item -> item.getKodeItem().equals(kodeItemToDelete));
        System.out.println("=======================================");
    }
}
