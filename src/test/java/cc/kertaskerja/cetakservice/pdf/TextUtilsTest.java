package cc.kertaskerja.cetakservice.pdf;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextUtilsTest {

    @Test
    void should_replace_characters_not_supported_by_helvetica_when_wrapping() throws Exception {
        PDType1Font helvetica = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        List<String> lines = TextUtils.wrapText("Indikator selesai ✅", helvetica, 8f, 200f);

        assertEquals(List.of("Indikator selesai ?"), lines);
    }

    @Test
    void should_preserve_newlines_when_replacing_unsupported_characters() throws Exception {
        PDType1Font helvetica = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        List<String> lines = TextUtils.wrapText("Nama pohon\n\nINDIKATOR\nSelesai ✅", helvetica, 8f, 200f);

        assertEquals(List.of("Nama pohon", "", "INDIKATOR", "Selesai ?"), lines);
    }
}
