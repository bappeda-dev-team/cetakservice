package cc.kertaskerja.cetakservice.pdf.pokin;

import java.awt.Color;

public enum JenisPohon {
    // PEMDA
    TEMATIK("Tematik", Palette.NETRAL),
    SUB_TEMATIK("Sub Tematik", Palette.NETRAL),
    SUB_SUB_TEMATIK("Sub Sub Tematik", Palette.NETRAL),
    STRATEGIC_PEMDA("Startegic Pemda", Palette.MERAH),
    TACTICAL_PEMDA("Tactical Pemda", Palette.BIRU),
    OPERATIONAL_PEMDA("Operational Pemda", Palette.HIJAU),
    // OPD
    OPD("OPD", Palette.NETRAL),
    TUJUAN("Tujuan", Palette.NETRAL),
    STRATEGIC("Startegic", Palette.MERAH),
    TACTICAL("Tactical", Palette.BIRU),
    OPERATIONAL("Operational", Palette.HIJAU),
    OPERATIONAL_N("Operational N", Palette.HIJAU),
    // BASE CASE
    POHON_KINERJA("POHON KINERJA", Palette.NETRAL);


    private final String label;
    private final Color headerColor;

    JenisPohon(String label, Color headerColor) {
        this.label = label;
        this.headerColor = headerColor;
    }

    public String getLabel() {
        return label;
    }

    public Color getHeaderColor() {
        return headerColor;
    }

    private static final class Palette {
        static final Color MERAH = new Color(0xCA3636);
        static final Color BIRU = new Color(0x3673CA);
        static final Color HIJAU = new Color(0x139052);
        static final Color NETRAL = new Color(0xFAFAF2);
    }
}
