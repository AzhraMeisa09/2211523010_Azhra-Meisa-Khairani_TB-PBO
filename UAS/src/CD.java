// Class CD yang merupakan turunan dari ItemPerpustakaan dan mengimplementasi HitungBiayaSewa
class CD extends ItemPerpustakaan implements HitungBiayaSewa {
    private int durasi;

    // Konstruktor dengan parameter dari superclass dan durasi CD
    public CD(String kodeItem, String judul, double hargaSewa, int durasi) {
        super(kodeItem, judul, hargaSewa);
        this.durasi = durasi;
    }

    // Getter untuk mendapatkan durasi
    public int getDurasi() {
        return durasi;
    }

    // Setter untuk mengatur durasi
    public void setDurasi(int durasi) {
        this.durasi = durasi;
    }

    // Override metode displayInfo dari ItemPerpustakaan
    @Override
    public void displayInfo() {
        System.out.println("CD - Kode: " + kodeItem);
        System.out.println("Judul: " + judul);
        System.out.println("Harga Sewa: " + hargaSewa);
        System.out.println("Durasi (menit): " + durasi);
        System.out.println("------------------------------");
    }

    // Override metode hitungBiayaSewa dari interface HitungBiayaSewa
    @Override
    public double hitungBiayaSewa(int jumlahHari) {
        return hargaSewa * jumlahHari;
    }
}
