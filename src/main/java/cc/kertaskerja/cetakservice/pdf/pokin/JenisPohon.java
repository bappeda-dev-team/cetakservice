package cc.kertaskerja.cetakservice.pdf.pokin;


import java.awt.Color;

public enum JenisPohon {
    // PEMDA
    TEMATIK("Tematik", Palette.NETRAL, Palette.BLACK),
    SUB_TEMATIK("Sub Tematik", Palette.NETRAL, Palette.BLACK),
    SUB_SUB_TEMATIK("Sub Sub Tematik", Palette.NETRAL, Palette.BLACK),
    STRATEGIC_PEMDA("Startegic Pemda", Palette.MERAH, Palette.NETRAL),
    TACTICAL_PEMDA("Tactical Pemda", Palette.BIRU, Palette.NETRAL),
    OPERATIONAL_PEMDA("Operational Pemda", Palette.HIJAU, Palette.NETRAL),
    // OPD
    OPD("Tujuan OPD", Palette.NETRAL, Palette.BLACK),
    TUJUAN("Tujuan", Palette.NETRAL, Palette.BLACK),
    STRATEGIC("Startegic", Palette.MERAH, Palette.NETRAL),
    TACTICAL("Tactical", Palette.BIRU, Palette.NETRAL),
    OPERATIONAL("Operational", Palette.HIJAU, Palette.NETRAL),
    OPERATIONAL_N("Operational N", Palette.HIJAU, Palette.NETRAL),
    // CROSSCUTTING
    STRATEGIC_CROSSCUTTING("Startegic Crosscuttig", Palette.MERAH, Palette.NETRAL),
    TACTICAL_CROSSCUTTING("Tactical Crosscutting", Palette.BIRU, Palette.NETRAL),
    OPERATIONAL_CROSSCUTTING("Operational Crosscutting", Palette.HIJAU, Palette.NETRAL),
    OPERATIONAL_N_CROSSCUTTING("Operational N Crosscutting", Palette.HIJAU, Palette.NETRAL),
    // BASE CASE
    POHON_KINERJA("POHON KINERJA", Palette.NETRAL, Palette.BLACK);


    private final String label;
    private final Color headerColor;
    private final Color textColor;

    JenisPohon(String label, Color headerColor, Color textColor) {
        this.label = label;
        this.headerColor = headerColor;
        this.textColor = textColor;
    }

    public String getLabel() {
        return label;
    }

    public Color getHeaderColor() {
        return headerColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    private static final class Palette {
        static final Color MERAH = new Color(0xCA3636);
        static final Color BIRU = new Color(0x3673CA);
        static final Color HIJAU = new Color(0x139052);
        static final Color NETRAL = new Color(0xFAFAF2);
        static final Color BLACK = new Color(0x1B0C0C);
    }
}
