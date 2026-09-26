package com.customshop.model;

public class TransaksiSewa extends Transaksi {
    private int durasiHari;
    private double uangDeposit;
    private int hariTerlambat;
    private double dendaPerHari;
    private boolean isSelesai;
    private double diskonPromoPersen;

    // Overloading Constructor 1: Tanpa diskon promosi sewa
    public TransaksiSewa(String kodeTransaksi, String namaPelanggan, Gitar gitar, String tanggalTransaksi, 
                         int durasiHari, double uangDeposit) {
        this(kodeTransaksi, namaPelanggan, gitar, tanggalTransaksi, durasiHari, uangDeposit, 0.0);
    }

    // Overloading Constructor 2: Dengan diskon promosi sewa (%)
    public TransaksiSewa(String kodeTransaksi, String namaPelanggan, Gitar gitar, String tanggalTransaksi, 
                         int durasiHari, double uangDeposit, double diskonPromoPersen) {
        super(kodeTransaksi, namaPelanggan, gitar, tanggalTransaksi);
        this.durasiHari = durasiHari;
        this.uangDeposit = uangDeposit;
        this.hariTerlambat = 0;
        this.dendaPerHari = gitar.getTarifSewaPerHari() * 1.5; 
        this.isSelesai = false;
        this.diskonPromoPersen = diskonPromoPersen;
        this.totalBiaya = hitungTotalBiaya();
    }

    @Override
    public double hitungTotalBiaya() {
        // Memanfaatkan Method Overloading hitungBiayaSewa milik class Gitar
        double biayaSewa = (diskonPromoPersen > 0)
                ? gitar.hitungBiayaSewa(durasiHari, diskonPromoPersen)
                : gitar.hitungBiayaSewa(durasiHari);
        double totalDenda = hariTerlambat * dendaPerHari;
        return biayaSewa + uangDeposit + totalDenda;
    }

    public void prosesPengembalian(int terlambat) {
        this.hariTerlambat = terlambat;
        this.isSelesai = true;
        this.gitar.setTersedia(true); 
        this.totalBiaya = hitungTotalBiaya();
    }

    @Override
    public void cetakNota() {
        double biayaSewa = (diskonPromoPersen > 0)
                ? gitar.hitungBiayaSewa(durasiHari, diskonPromoPersen)
                : gitar.hitungBiayaSewa(durasiHari);
        double totalDenda = hariTerlambat * dendaPerHari;
        double sisaDepositDikembalikan = Math.max(0, uangDeposit - totalDenda);

        System.out.println("=================================================================");
        System.out.println("               BUKTI KONTRAK RENTAL GITAR CUSTOM                 ");
        System.out.println("=================================================================");
        System.out.printf(" No. Kontrak Sewa : %s%n", kodeTransaksi);
        System.out.printf(" Tanggal Mulai    : %s%n", tanggalTransaksi);
        System.out.printf(" Nama Penyewa     : %s%n", namaPelanggan);
        System.out.println("-----------------------------------------------------------------");
        System.out.printf(" Unit Gitar       : [%s] %s %s%n", gitar.getIdGitar(), gitar.getMerk(), gitar.getModel());
        System.out.printf(" Tarif Sewa       : Rp %,14.0f / hari%n", gitar.getTarifSewaPerHari());
        System.out.printf(" Durasi Sewa      : %d Hari%n", durasiHari);
        if (diskonPromoPersen > 0) {
            System.out.printf(" Diskon Sewa (%.0f%%): -Rp %,13.0f%n", diskonPromoPersen, (gitar.hitungBiayaSewa(durasiHari) * (diskonPromoPersen / 100.0)));
        }
        System.out.printf(" Subtotal Sewa    : Rp %,14.0f%n", biayaSewa);
        System.out.printf(" Deposit Jaminan  : Rp %,14.0f (Refundable)%n", uangDeposit);
        if (hariTerlambat > 0) {
            System.out.printf(" Keterlambatan    : %d Hari (Denda: Rp %,.0f)%n", hariTerlambat, totalDenda);
        }
        System.out.println("-----------------------------------------------------------------");
        System.out.printf(" STATUS RENTAL    : %s%n", (isSelesai ? "SELESAI (GITAR TELAH DIKEMBALIKAN)" : "AKTIF (GITAR SEDANG DIPINJAM)"));
        if (isSelesai) {
            System.out.printf(" PENGEMBALIAN DEPOSIT : Rp %,14.0f%n", sisaDepositDikembalikan);
        }
        System.out.println("=================================================================");
    }

    public int getDurasiHari() { return durasiHari; }
    public double getUangDeposit() { return uangDeposit; }
    public int getHariTerlambat() { return hariTerlambat; }
    public boolean isSelesai() { return isSelesai; }
    public double getDiskonPromoPersen() { return diskonPromoPersen; }
}
