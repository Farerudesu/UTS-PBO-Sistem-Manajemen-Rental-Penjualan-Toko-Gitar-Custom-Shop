package com.customshop.service;

import com.customshop.model.Gitar;
import com.customshop.model.GitarAkustik;
import com.customshop.model.GitarBass;
import com.customshop.model.GitarElektrik;
import com.customshop.model.GitarVintageRelic;
import com.customshop.model.Transaksi;
import com.customshop.model.TransaksiBeli;
import com.customshop.model.TransaksiSewa;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuitarStoreService {
    private List<Gitar> daftarGitar;
    private List<Transaksi> riwayatTransaksi;
    private int counterTransaksi;

    public GuitarStoreService() {
        this.daftarGitar = new ArrayList<>();
        this.riwayatTransaksi = new ArrayList<>();
        this.counterTransaksi = 1001;
        initDummyData();
    }

    private void initDummyData() {
        // Gitar Elektrik
        daftarGitar.add(new GitarElektrik(
                "EL-01", "Fender", "Custom Shop '60s Stratocaster Relic", 
                "Selected Lightweight Alder", 38500000, 250000, 
                "Custom Shop Hand-Wound '60s SSS", "Vintage Synchronized Tremolo", false));
        
        daftarGitar.add(new GitarElektrik(
                "EL-02", "Gibson", "Les Paul Custom 1957 Black Beauty", 
                "One-Piece Solid Mahogany", 54000000, 320000, 
                "Custom Bucker Alnico III HH", "Tune-O-Matic with Stopbar", true));

        // Gitar Vintage Relic (Multilevel Inheritance: Gitar -> GitarElektrik -> GitarVintageRelic)
        daftarGitar.add(new GitarVintageRelic(
                "VR-01", "Fender", "1960 Stratocaster Heavy Relic MB", 
                "Selected 2-Piece Alder (Aged)", 68000000, 450000, 
                "Custom Shop Hand-Wound Texas Special", "Vintage Tremolo (Relic Nickel)", false, 
                "Heavy Relic (Aged Nitrocellulose)", "Dale Wilson (Principal Master Builder)", 1960));

        daftarGitar.add(new GitarAkustik(
                "AK-01", "Taylor", "814ce Grand Auditorium Custom", 
                "East Indian Rosewood", 46000000, 280000, 
                "Solid Lutz Spruce", "Taylor Expression System 2 (ES2)", true));

        daftarGitar.add(new GitarAkustik(
                "AK-02", "Martin", "D-28 Modern Deluxe Custom Shop", 
                "East Indian Rosewood", 49500000, 300000, 
                "Solid Adirondack Spruce VTS", "Fishman Aura VT Blend", false));

        daftarGitar.add(new GitarBass(
                "BS-01", "EB Music Man", "StingRay Special 5-String", 
                "Select Ash Body", 36000000, 220000, 
                5, true, "Single Neodymium Humbucker"));

        daftarGitar.add(new GitarBass(
                "BS-02", "Fender", "Custom Shop '64 Jazz Bass Journeyman", 
                "Selected Premium Alder", 42000000, 260000, 
                4, false, "Custom Shop Hand-Wound '64 J-Bass"));
    }

    public List<Gitar> getDaftarGitar() {
        return daftarGitar;
    }

    public Gitar cariGitarById(String id) {
        for (Gitar g : daftarGitar) {
            if (g.getIdGitar().equalsIgnoreCase(id.trim())) {
                return g;
            }
        }
        return null;
    }

    // Method Overloading 1: Menampilkan seluruh katalog instrumen
    public void tampilkanKatalog() {
        tampilkanKatalog("SEMUA");
    }

    // Method Overloading 2: Menampilkan katalog berdasarkan filter kategori instrumen
    public void tampilkanKatalog(String filterKategori) {
        System.out.println("\n=========================================================================================");
        System.out.println("                     KATALOG KOLEKSI GITAR CUSTOM SHOP PREMIER                           ");
        if (!filterKategori.equalsIgnoreCase("SEMUA")) {
            System.out.printf("                              FILTER KATEGORI: %s%n", filterKategori.toUpperCase());
        }
        System.out.println("=========================================================================================");
        System.out.printf("%-7s | %-12s | %-32s | %-16s | %-14s | %-10s%n", 
                "ID", "MERK", "SERI / MODEL", "HARGA BELI", "SEWA/HARI", "STATUS");
        System.out.println("-----------------------------------------------------------------------------------------");
        for (Gitar g : daftarGitar) {
            boolean tampil = false;
            if (filterKategori.equalsIgnoreCase("SEMUA")) {
                tampil = true;
            } else if (filterKategori.equalsIgnoreCase("ELEKTRIK") && (g instanceof GitarElektrik) && !(g instanceof GitarVintageRelic)) {
                tampil = true;
            } else if (filterKategori.equalsIgnoreCase("AKUSTIK") && (g instanceof GitarAkustik)) {
                tampil = true;
            } else if (filterKategori.equalsIgnoreCase("BASS") && (g instanceof GitarBass)) {
                tampil = true;
            } else if (filterKategori.equalsIgnoreCase("RELIC") && (g instanceof GitarVintageRelic)) {
                tampil = true;
            }

            if (tampil) {
                System.out.printf("%-7s | %-12s | %-32s | Rp %,13.0f | Rp %,10.0f | %-10s%n",
                        g.getIdGitar(),
                        g.getMerk(),
                        (g.getModel().length() > 32 ? g.getModel().substring(0, 29) + "..." : g.getModel()),
                        g.getHargaBeli(),
                        g.getTarifSewaPerHari(),
                        (g.isTersedia() ? "[TERSEDIA]" : "[DISEWA]"));
            }
        }
        System.out.println("=========================================================================================");
    }

    public TransaksiBeli beliGitar(String namaCustomer, String idGitar, double diskon, boolean hardcase) {
        Gitar gitar = cariGitarById(idGitar);
        if (gitar == null) {
            System.out.println(" >> Error: ID Gitar tidak ditemukan!");
            return null;
        }
        if (!gitar.isTersedia()) {
            System.out.println(" >> Maaf, gitar ini sedang disewa oleh pelanggan lain dan belum dapat dibeli!");
            return null;
        }

        String kodeTrx = "TRX-BUY-" + (counterTransaksi++);
        String tanggal = getTanggalHariIni();

        TransaksiBeli trx = new TransaksiBeli(kodeTrx, namaCustomer, gitar, tanggal, diskon, hardcase, 24);
        gitar.setTersedia(false); // Setelah dibeli, unit keluar dari stok aktif
        riwayatTransaksi.add(trx);
        return trx;
    }

    // Method Overloading 1: Transaksi sewa standar
    public TransaksiSewa sewaGitar(String namaCustomer, String idGitar, int durasiHari) {
        return sewaGitar(namaCustomer, idGitar, durasiHari, 0.0);
    }

    // Method Overloading 2: Transaksi sewa dengan promo potongan sewa
    public TransaksiSewa sewaGitar(String namaCustomer, String idGitar, int durasiHari, double diskonPromoPersen) {
        Gitar gitar = cariGitarById(idGitar);
        if (gitar == null) {
            System.out.println(" >> Error: ID Gitar tidak ditemukan!");
            return null;
        }
        if (!gitar.isTersedia()) {
            System.out.println(" >> Maaf, gitar ini sedang dalam masa sewa pelanggan lain!");
            return null;
        }

        String kodeTrx = "TRX-RNT-" + (counterTransaksi++);
        String tanggal = getTanggalHariIni();
        double deposit = gitar.getTarifSewaPerHari() * 2; // Uang jaminan = 2x tarif harian

        TransaksiSewa trx = new TransaksiSewa(kodeTrx, namaCustomer, gitar, tanggal, durasiHari, deposit, diskonPromoPersen);
        gitar.setTersedia(false); // Gitar disewa
        riwayatTransaksi.add(trx);
        return trx;
    }

    public TransaksiSewa cariTransaksiSewaAktif(String kodeTrx) {
        for (Transaksi t : riwayatTransaksi) {
            if (t instanceof TransaksiSewa && t.getKodeTransaksi().equalsIgnoreCase(kodeTrx.trim())) {
                TransaksiSewa sewa = (TransaksiSewa) t;
                if (!sewa.isSelesai()) {
                    return sewa;
                }
            }
        }
        return null;
    }

    public void tampilkanRiwayat() {
        if (riwayatTransaksi.isEmpty()) {
            System.out.println(" >> Belum ada riwayat transaksi yang tercatat.");
            return;
        }

        System.out.println("\n=========================================================================================");
        System.out.println("                                RIWAYAT SEMUA TRANSAKSI                                  ");
        System.out.println("=========================================================================================");
        for (Transaksi t : riwayatTransaksi) {
            t.cetakNota();
            System.out.println();
        }
    }

    private String getTanggalHariIni() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return sdf.format(new Date());
    }
}
