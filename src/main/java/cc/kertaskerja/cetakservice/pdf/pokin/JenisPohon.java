package cc.kertaskerja.cetakservice.pdf.pokin;

import java.awt.Color;

public enum JenisPohon {
    // PEMDA
    TEMATIK("Tematik", Palette.NETRAL, Palette.NETRAL, Palette.BLACK),
    SUB_TEMATIK("Sub Tematik", Palette.NETRAL, Palette.NETRAL, Palette.BLACK),
    SUB_SUB_TEMATIK("Sub Sub Tematik", Palette.NETRAL, Palette.NETRAL, Palette.BLACK),
    STRATEGIC_PEMDA("Startegic Pemda", Palette.MERAH_AWAL, Palette.MERAH_AKHIR, Palette.NETRAL),
    TACTICAL_PEMDA("Tactical Pemda", Palette.BIRU_AWAL, Palette.BIRU_AKHIR, Palette.NETRAL),
    OPERATIONAL_PEMDA("Operational Pemda", Palette.HIJAU_AWAL, Palette.HIJAU_AKHIR, Palette.NETRAL),
    // OPD
    OPD("Tujuan OPD", Palette.NETRAL, Palette.NETRAL, Palette.BLACK),
    TUJUAN("Tujuan", Palette.NETRAL, Palette.NETRAL, Palette.BLACK),
    STRATEGIC("Startegic", Palette.MERAH_AWAL, Palette.MERAH_AWAL, Palette.NETRAL),
    TACTICAL("Tactical", Palette.BIRU_AWAL, Palette.BIRU_AWAL, Palette.NETRAL),
    OPERATIONAL("Operational", Palette.HIJAU_AKHIR, Palette.HIJAU_AKHIR, Palette.NETRAL),
    OPERATIONAL_N("Operational N", Palette.NETRAL, Palette.NETRAL, Palette.HIJAU_AKHIR),
    // CROSSCUTTING
    STRATEGIC_CROSSCUTTING("Startegic Crosscuttig", Palette.NETRAL, Palette.NETRAL, Palette.MERAH_AWAL),
    TACTICAL_CROSSCUTTING("Tactical Crosscutting", Palette.NETRAL, Palette.NETRAL, Palette.BIRU_AWAL),
    OPERATIONAL_CROSSCUTTING("Operational Crosscutting", Palette.NETRAL, Palette.NETRAL, Palette.HIJAU_AWAL),
    OPERATIONAL_N_CROSSCUTTING("Operational N Crosscutting", Palette.NETRAL, Palette.NETRAL, Palette.HIJAU_AKHIR),
    // BASE CASE
    POHON_KINERJA("POHON KINERJA", Palette.NETRAL, Palette.NETRAL, Palette.BLACK);

    private final String label;
    private final Color colorAwal;
    private final Color colorAkhir;
    private final Color textColor;

    JenisPohon(String label, Color colorAwal, Color colorAkhir, Color textColor) {
        this.label = label;
        this.colorAwal = colorAwal;
        this.colorAkhir = colorAkhir;
        this.textColor = textColor;
    }

    public String getLabel() {
        return label;
    }

    public Color getColorAwal() {
        return colorAwal;
    }
    public Color getColorAkhir() {
        return colorAkhir;
    }

    public Color getTextColor() {
        return textColor;
    }

    public static final class Palette {
        static final Color MERAH_AWAL = new Color(0xCA3636);
        static final Color MERAH_AKHIR = new Color(0xBD04A1);
        static final Color BIRU_AWAL = new Color(0x3673CA);
        static final Color BIRU_AKHIR = new Color(0x08D2FB);
        static final Color HIJAU_AWAL = new Color(0x007982);
        static final Color HIJAU_AKHIR = new Color(0x2DCB06);
        static final Color BLACK = new Color(0x1B0C0C);
        static final Color NETRAL = new Color(0xFAFAF2);
    }
}
