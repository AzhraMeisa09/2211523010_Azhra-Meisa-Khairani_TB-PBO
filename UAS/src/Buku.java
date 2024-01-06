import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Class Buku yang merupakan turunan dari ItemPerpustakaan
class Buku extends ItemPerpustakaan {
    private int jumlahHalaman;
    private Date tanggalTerbit;

    // Konstruktor dengan tambahan parameter jumlah halaman dan tanggalTerbit
    public Buku(String kodeItem, String judul, double hargaSewa, int jumlahHalaman, String tanggalTerbit) {
        super(kodeItem, judul, hargaSewa);
        this.jumlahHalaman = jumlahHalaman;
        this.tanggalTerbit = parseTanggal(tanggalTerbit);
    }

    // Getter untuk mendapatkan jumlahHalaman
    public int getJumlahHalaman() {
        return jumlahHalaman;
    }

    // Setter untuk mengatur jumlahHalaman
    public void setJumlahHalaman(int jumlahHalaman) {
        this.jumlahHalaman = jumlahHalaman;
    }

    // Getter untuk mendapatkan tanggalTerbit
    public Date getTanggalTerbit() {
        return tanggalTerbit;
    }

    // Metode untuk mengonversi String menjadi Date
    public static Date parseTanggal(String text) {
        if (text == null) {
            return null;
        }

        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(text);
        } catch (ParseException e) {
            System.out.println("Gagal parsing tanggal: " + e.getMessage());
            return null; 
        }
    }

    // Override metode displayInfo dari ItemPerpustakaan
    @Override
    public void displayInfo() {
        System.out.println("Buku - Kode: " + kodeItem);
        System.out.println("Judul: " + judul);
        System.out.println("Harga Sewa: " + hargaSewa);
        System.out.println("Jumlah Halaman: " + jumlahHalaman);
        System.out.println("Tanggal Terbit: " + (tanggalTerbit != null ? new SimpleDateFormat("dd-MM-yyyy").format(tanggalTerbit) : "-"));
        System.out.println("------------------------------");
    }
}
