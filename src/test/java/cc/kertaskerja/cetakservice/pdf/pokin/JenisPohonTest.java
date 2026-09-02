package cc.kertaskerja.cetakservice.pdf.pokin;

import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JenisPohonTest {

    @Test
    void uses_the_requested_tailwind_header_colours() {
        assertEquals(new Color(0xB91C1C), JenisPohon.STRATEGIC.getHeaderColor());
        assertEquals(new Color(0x3B82F6), JenisPohon.TACTICAL.getHeaderColor());
        assertEquals(new Color(0x22C55E), JenisPohon.OPERATIONAL.getHeaderColor());
        assertEquals(Color.WHITE, JenisPohon.OPERATIONAL_N.getHeaderColor());
        assertEquals(new Color(0xCA3636), JenisPohon.STRATEGIC_PEMDA.getHeaderColor());
        assertEquals(new Color(0xBD04A1), JenisPohon.STRATEGIC_PEMDA.getHeaderGradientEndColor());
        assertEquals(Color.WHITE, JenisPohon.TACTICAL_PEMDA.getTextColor());
        assertEquals(Color.WHITE, JenisPohon.OPERATIONAL.getTextColor());
        assertEquals(new Color(0x22C55E), JenisPohon.OPERATIONAL_N.getTextColor());
        assertEquals(new Color(0xB91C1C), JenisPohon.STRATEGIC_CROSSCUTTING.getTextColor());
    }
}
