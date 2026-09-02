package cc.kertaskerja.cetakservice.pdf.pokin;


import java.awt.Color;

public enum JenisPohon {
    // PEMDA
    TEMATIK("Tematik", Palette.NETRAL, Palette.BLACK),
    SUB_TEMATIK("Sub Tematik", Palette.NETRAL, Palette.BLACK),
    SUB_SUB_TEMATIK("Sub Sub Tematik", Palette.NETRAL, Palette.BLACK),
    STRATEGIC_PEMDA("Startegic Pemda", Palette.PEMDA_STRATEGIC_FROM, Palette.PEMDA_STRATEGIC_TO, Palette.WHITE),
    TACTICAL_PEMDA("Tactical Pemda", Palette.PEMDA_TACTICAL_FROM, Palette.PEMDA_TACTICAL_TO, Palette.WHITE),
    OPERATIONAL_PEMDA("Operational Pemda", Palette.PEMDA_OPERATIONAL_FROM, Palette.PEMDA_OPERATIONAL_TO, Palette.WHITE),
    // OPD
    OPD("Tujuan OPD", Palette.NETRAL, Palette.BLACK),
    TUJUAN("Tujuan", Palette.NETRAL, Palette.BLACK),
    // Jenis OPD utama mempertahankan warna header yang telah digunakan sebelumnya.
    STRATEGIC("Startegic", Palette.RED_700, Palette.WHITE),
    TACTICAL("Tactical", Palette.BLUE_500, Palette.WHITE),
    OPERATIONAL("Operational", Palette.GREEN_500, Palette.WHITE),
    OPERATIONAL_N("Operational N", Palette.WHITE, Palette.GREEN_500),
    // CROSSCUTTING
    STRATEGIC_CROSSCUTTING("Startegic Crosscuttig", Palette.WHITE, Palette.RED_700),
    TACTICAL_CROSSCUTTING("Tactical Crosscutting", Palette.WHITE, Palette.BLUE_500),
    OPERATIONAL_CROSSCUTTING("Operational Crosscutting", Palette.WHITE, Palette.GREEN_500),
    OPERATIONAL_N_CROSSCUTTING("Operational N Crosscutting", Palette.WHITE, Palette.GREEN_500),
    // BASE CASE
    POHON_KINERJA("POHON KINERJA", Palette.NETRAL, Palette.BLACK);


    private final String label;
    private final Color headerColor;
    private final Color headerGradientEndColor;
    private final Color textColor;

    JenisPohon(String label, Color headerColor, Color textColor) {
        this(label, headerColor, headerColor, textColor);
    }

    JenisPohon(String label, Color headerColor, Color headerGradientEndColor, Color textColor) {
        this.label = label;
        this.headerColor = headerColor;
        this.headerGradientEndColor = headerGradientEndColor;
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

    public Color getHeaderGradientEndColor() {
        return headerGradientEndColor;
    }

    private static final class Palette {
        // Nilai RGB Tailwind default dan warna khusus gradient Pemda.
        static final Color RED_700 = new Color(0xB91C1C);
        static final Color BLUE_500 = new Color(0x3B82F6);
        static final Color GREEN_500 = new Color(0x22C55E);
        static final Color PEMDA_STRATEGIC_FROM = new Color(0xCA3636);
        static final Color PEMDA_STRATEGIC_TO = new Color(0xBD04A1);
        static final Color PEMDA_TACTICAL_FROM = new Color(0x3673CA);
        static final Color PEMDA_TACTICAL_TO = new Color(0x08D2FB);
        static final Color PEMDA_OPERATIONAL_FROM = new Color(0x007982);
        static final Color PEMDA_OPERATIONAL_TO = new Color(0x2DCB06);
        static final Color WHITE = new Color(0xFFFFFF);
        static final Color NETRAL = new Color(0xFAFAF2);
        static final Color BLACK = new Color(0x1B0C0C);
    }
}
