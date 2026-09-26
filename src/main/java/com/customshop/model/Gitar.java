package com.customshop.model;

public abstract class Gitar {
    protected String idGitar;
    protected String merk;
    protected String model;
    protected String jenisKayuBody;
    protected double hargaBeli;
    protected double tarifSewaPerHari;
    protected boolean isTersedia;

    public Gitar(String idGitar, String merk, String model, String jenisKayuBody, double hargaBeli, double tarifSewaPerHari) {
        this.idGitar = idGitar;
        this.merk = merk;
        this.model = model;
        this.jenisKayuBody = jenisKayuBody;
        this.hargaBeli = hargaBeli;
        this.tarifSewaPerHari = tarifSewaPerHari;
        this.isTersedia = true; // Default selalu tersedia saat terdaftar
    }

    public abstract void displaySpesifikasi();

    // Method Overloading 1: Perhitungan biaya sewa standar (durasi * tarif harian)
    public double hitungBiayaSewa(int durasiHari) {
        return this.tarifSewaPerHari * durasiHari;
    }

    // Method Overloading 2: Perhitungan biaya sewa dengan potongan diskon promosi (persen)
    public double hitungBiayaSewa(int durasiHari, double diskonPromoPersen) {
        double totalStandar = hitungBiayaSewa(durasiHari);
        double potongan = totalStandar * (diskonPromoPersen / 100.0);
        return totalStandar - potongan;
    }

    public String getInfoSingkat() {
        return String.format("[%s] %s %s - Beli: Rp %,.0f | Sewa: Rp %,.0f/hari | Status: %s",
                idGitar, merk, model, hargaBeli, tarifSewaPerHari, (isTersedia ? "TERSEDIA" : "DISEWA"));
    }

    public String getIdGitar() { return idGitar; }
    public String getMerk() { return merk; }
    public String getModel() { return model; }
    public String getJenisKayuBody() { return jenisKayuBody; }
    public double getHargaBeli() { return hargaBeli; }
    public double getTarifSewaPerHari() { return tarifSewaPerHari; }
    public boolean isTersedia() { return isTersedia; }
    public void setTersedia(boolean tersedia) { this.isTersedia = tersedia; }
}
