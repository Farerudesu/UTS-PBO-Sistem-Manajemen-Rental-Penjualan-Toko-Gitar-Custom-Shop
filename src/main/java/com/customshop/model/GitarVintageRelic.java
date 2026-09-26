package com.customshop.model;

/**
 * Class GitarVintageRelic menerapkan MULTILEVEL INHERITANCE:
 * Gitar (Superclass Level 1) -> GitarElektrik (Subclass Level 2) -> GitarVintageRelic (Subclass Level 3).
 * Mengkhususkan instrumen edisi kolektor Masterbuilt dengan tingkat relic aging dan tahun reissue.
 */
public class GitarVintageRelic extends GitarElektrik {
    private String relicAgingLevel;
    private String masterBuilder;
    private int tahunReissueVintage;

    public GitarVintageRelic(String idGitar, String merk, String model, String jenisKayuBody, 
                             double hargaBeli, double tarifSewaPerHari, 
                             String tipePickup, String bridgeType, boolean hasCoilSplit,
                             String relicAgingLevel, String masterBuilder, int tahunReissueVintage) {
        super(idGitar, merk, model, jenisKayuBody, hargaBeli, tarifSewaPerHari, tipePickup, bridgeType, hasCoilSplit);
        this.relicAgingLevel = relicAgingLevel;
        this.masterBuilder = masterBuilder;
        this.tahunReissueVintage = tahunReissueVintage;
    }

    @Override
    public void displaySpesifikasi() {
        System.out.println("-----------------------------------------------------------------");
        System.out.printf(" Kategori     : GITAR ELEKTRIK VINTAGE RELIC (MASTERBUILT)%n");
        System.out.printf(" ID Instrumen : %s%n", idGitar);
        System.out.printf(" Seri & Merk  : %s %s (%d Reissue)%n", merk, model, tahunReissueVintage);
        System.out.printf(" Masterbuilder: %s%n", masterBuilder);
        System.out.printf(" Relic Finish : %s%n", relicAgingLevel);
        System.out.printf(" Kayu Body    : %s%n", jenisKayuBody);
        System.out.printf(" Tipe Pickup  : %s%n", getTipePickup());
        System.out.printf(" Bridge/Trem  : %s%n", getBridgeType());
        System.out.printf(" Fitur Khusus : %s%n", (isHasCoilSplit() ? "Coil-Split Switch" : "Period-Correct Vintage Wiring"));
        System.out.printf(" Harga Beli   : Rp %,.0f%n", hargaBeli);
        System.out.printf(" Tarif Sewa   : Rp %,.0f / hari%n", tarifSewaPerHari);
        System.out.printf(" Status       : %s%n", (isTersedia ? "READY FOR SALE / RENT" : "CURRENTLY RENTED OUT"));
        System.out.println("-----------------------------------------------------------------");
    }

    public String getRelicAgingLevel() { return relicAgingLevel; }
    public String getMasterBuilder() { return masterBuilder; }
    public int getTahunReissueVintage() { return tahunReissueVintage; }
}
