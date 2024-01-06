// Kelas abstrak yang merepresentasikan suatu item di perpustakaan
abstract class ItemPerpustakaan {
    protected String kodeItem;
    protected String judul;
    protected double hargaSewa;

    // Konstruktor untuk menginisialisasi kode item, judul, dan harga sewa
    public ItemPerpustakaan(String kodeItem, String judul, double hargaSewa) {
        this.kodeItem = kodeItem;
        this.judul = judul;
        this.hargaSewa = hargaSewa;
    }

    // Metode abstrak untuk menampilkan informasi item
    public abstract void displayInfo();

    // Getter untuk mendapatkan kode item
    public String getKodeItem() {
        return kodeItem;
    }

    // Setter untuk mengatur judul item
    public void setJudul(String judul) {
        this.judul = judul;
    }

    // Getter untuk mendapatkan judul item
    public String getJudul() {
        return judul;
    }

    // Setter untuk mengatur harga sewa item
    public void setHargaSewa(double hargaSewa) {
        this.hargaSewa = hargaSewa;
    }

    // Getter untuk mendapatkan harga sewa item
    public double getHargaSewa() {
        return hargaSewa;
    }
}
