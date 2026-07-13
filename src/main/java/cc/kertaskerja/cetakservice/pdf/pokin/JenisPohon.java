package cc.kertaskerja.cetakservice.pdf.pokin;

public enum JenisPohon {
    // PEMDA
    TEMATIK("Tematik"),
    SUB_TEMATIK("Sub Tematik"),
    SUB_SUB_TEMATIK("Sub Sub Tematik"),
    STRATEGIC_PEMDA("Startegic Pemda"),
    TACTICAL_PEMDA("Tactical Pemda"),
    OPERATIONAL_PEMDA("Operational Pemda"),
    // OPD
    OPD("OPD"),
    TUJUAN("Tujuan"),
    STRATEGIC("Startegic"),
    TACTICAL("Tactical"),
    OPERATIONAL("Operational"),
    OPERATIONAL_N("Operational N"),
    // BASE CASE
    POHON_KINERJA("POHON KINERJA");


    private final String label;

    JenisPohon(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
