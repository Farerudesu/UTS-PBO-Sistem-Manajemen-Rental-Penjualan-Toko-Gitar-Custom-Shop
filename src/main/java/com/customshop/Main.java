package com.customshop;

import com.customshop.model.Gitar;
import com.customshop.model.TransaksiBeli;
import com.customshop.model.TransaksiSewa;
import com.customshop.service.GuitarStoreService;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GuitarStoreService storeService = new GuitarStoreService();

        printHeader();

        boolean running = true;
        while (running) {
            System.out.println("\n=======================================================");
            System.out.println("            MENU UTAMA TOKO GITAR CUSTOM SHOP          ");
            System.out.println("=======================================================");
            System.out.println(" [1] Lihat Katalog Koleksi Gitar");
            System.out.println(" [2] Cek Spesifikasi Detail Instrumen");
            System.out.println(" [3] Transaksi Pembelian Unit Gitar");
            System.out.println(" [4] Transaksi Penyewaan / Rental Gitar");
            System.out.println(" [5] Pengembalian Gitar Rental & Cek Denda");
            System.out.println(" [6] Lihat Seluruh Riwayat Transaksi");
            System.out.println(" [7] Keluar Program");
            System.out.print(" Masukkan pilihan menu (1-7): ");

            String pilihan = scanner.nextLine().trim();

            switch (pilihan) {
                case "1":
                    menuLihatKatalog(storeService, scanner);
                    break;

                case "2":
                    menuCekSpesifikasi(storeService, scanner);
                    break;

                case "3":
                    menuBeliGitar(storeService, scanner);
                    break;

                case "4":
                    menuSewaGitar(storeService, scanner);
                    break;

                case "5":
                    menuPengembalianGitar(storeService, scanner);
                    break;

                case "6":
                    storeService.tampilkanRiwayat();
                    break;

                case "7":
                    System.out.println("\n >> Terima kasih telah mengunjungi CustomShopGuitar Sempaja. Keep on Rockin'!");
                    running = false;
                    break;

                default:
                    System.out.println(" >> Pilihan menu tidak valid! Silakan masukkan angka 1 sampai 7.");
            }
        }

        scanner.close();
    }

    private static void menuLihatKatalog(GuitarStoreService service, Scanner scanner) {
        System.out.println("\n [Filter Kategori: 1. Semua | 2. Elektrik | 3. Akustik | 4. Bass | 5. Vintage Relic]");
        System.out.print(" Pilih kategori (Enter untuk Semua): ");
        String filter = scanner.nextLine().trim();
        switch (filter) {
            case "2":
                service.tampilkanKatalog("ELEKTRIK");
                break;
            case "3":
                service.tampilkanKatalog("AKUSTIK");
                break;
            case "4":
                service.tampilkanKatalog("BASS");
                break;
            case "5":
                service.tampilkanKatalog("RELIC");
                break;
            default:
                service.tampilkanKatalog(); // Memanggil overloaded method versi tanpa parameter
                break;
        }
    }

    private static void menuCekSpesifikasi(GuitarStoreService service, Scanner scanner) {
        service.tampilkanKatalog();
        System.out.print("\n Masukkan ID Gitar yang ingin dicek spesifikasinya (misal: EL-01 / VR-01): ");
        String id = scanner.nextLine().trim();

        Gitar gitar = service.cariGitarById(id);
        if (gitar != null) {
            gitar.displaySpesifikasi();
        } else {
            System.out.println(" >> Gitar dengan ID tersebut tidak ditemukan!");
        }
    }

    private static void menuBeliGitar(GuitarStoreService service, Scanner scanner) {
        System.out.println("\n--- TRANSAKSI PEMBELIAN GITAR ---");
        service.tampilkanKatalog();

        System.out.print("\n Masukkan Nama Pelanggan : ");
        String nama = scanner.nextLine().trim();
        if (nama.isEmpty()) nama = "Customer Guest";

        System.out.print(" Masukkan ID Gitar yang ingin dibeli: ");
        String idGitar = scanner.nextLine().trim();

        System.out.print(" Masukkan Diskon Member (%) [Ketik 0 jika tidak ada]: ");
        double diskon = 0;
        try {
            diskon = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            diskon = 0;
        }

        System.out.print(" Tambah Deluxe Flight Hardcase (+Rp 750.000)? (y/n): ");
        boolean hardcase = scanner.nextLine().trim().equalsIgnoreCase("y");

        TransaksiBeli trx = service.beliGitar(nama, idGitar, diskon, hardcase);
        if (trx != null) {
            System.out.println("\n >> Pembelian BERHASIL diproses!");
            trx.cetakNota();
        }
    }

    private static void menuSewaGitar(GuitarStoreService service, Scanner scanner) {
        System.out.println("\n--- TRANSAKSI PENYEWAAN / RENTAL GITAR ---");
        service.tampilkanKatalog();

        System.out.print("\n Masukkan Nama Penyewa : ");
        String nama = scanner.nextLine().trim();
        if (nama.isEmpty()) nama = "Penyewa Guest";

        System.out.print(" Masukkan ID Gitar yang ingin disewa: ");
        String idGitar = scanner.nextLine().trim();

        System.out.print(" Masukkan Durasi Sewa (dalam hari): ");
        int durasi = 1;
        try {
            durasi = Integer.parseInt(scanner.nextLine().trim());
            if (durasi <= 0) durasi = 1;
        } catch (NumberFormatException e) {
            durasi = 1;
        }

        System.out.print(" Masukkan Diskon Promo Sewa (%) [Ketik 0 jika tidak ada]: ");
        double diskonPromo = 0;
        try {
            diskonPromo = Double.parseDouble(scanner.nextLine().trim());
            if (diskonPromo < 0) diskonPromo = 0;
        } catch (NumberFormatException e) {
            diskonPromo = 0;
        }

        TransaksiSewa trx;
        if (diskonPromo > 0) {
            trx = service.sewaGitar(nama, idGitar, durasi, diskonPromo); // Overloading versi promo
        } else {
            trx = service.sewaGitar(nama, idGitar, durasi); // Overloading versi standar
        }

        if (trx != null) {
            System.out.println("\n >> Transaksi sewa BERHASIL diproses!");
            trx.cetakNota();
        }
    }

    private static void menuPengembalianGitar(GuitarStoreService service, Scanner scanner) {
        System.out.println("\n--- PENGEMBALIAN GITAR RENTAL ---");
        System.out.print(" Masukkan Nomor Kontrak Sewa (contoh: TRX-RNT-1001): ");
        String kodeTrx = scanner.nextLine().trim();

        TransaksiSewa trx = service.cariTransaksiSewaAktif(kodeTrx);
        if (trx == null) {
            System.out.println(" >> Transaksi sewa aktif tidak ditemukan atau sudah diselesaikan sebelumnya!");
            return;
        }

        System.out.printf(" Kontrak Ditemukan: Penyewa %s | Unit: %s %s%n", 
                trx.getNamaPelanggan(), trx.getGitar().getMerk(), trx.getGitar().getModel());
        
        System.out.print(" Masukkan jumlah hari keterlambatan pengembalian (0 jika tepat waktu): ");
        int terlambat = 0;
        try {
            terlambat = Integer.parseInt(scanner.nextLine().trim());
            if (terlambat < 0) terlambat = 0;
        } catch (NumberFormatException e) {
            terlambat = 0;
        }

        trx.prosesPengembalian(terlambat);
        System.out.println("\n >> Unit gitar telah berhasil dikembalikan ke inventaris!");
        trx.cetakNota();
    }

    private static void printHeader() {
        System.out.println("=================================================================");
        System.out.println("         VINTAGE & CUSTOM SHOP GUITAR STORE SYSTEM               ");
        System.out.println("               Aplikasi Manajemen Rental & Penjualan             ");
        System.out.println("=================================================================");
    }
}
