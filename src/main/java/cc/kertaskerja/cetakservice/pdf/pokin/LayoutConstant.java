package cc.kertaskerja.cetakservice.pdf.pokin;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public final class LayoutConstant {
    public static final float BOX_WIDTH = 120f;
    public static final float BOX_HEIGHT = 80f;
    public static final float CROSSCUTTING_BOX_HEIGHT = 200f;
    public static final float TUJUAN_OPD_BOX_HEIGHT = 80f;

    public static final float SIBLING_GAP = 15f;
    public static final float LEVEL_GAP = 30f;

    public static final float PAGE_HEADER_HEIGHT = 10f;
    public static final float PAGE_HEADER_SPACING = 20f;
    public static final float PAGE_MARGIN_TOP = 30f;
    public static final float PAGE_MARGIN_LEFT = 30f;
    public static final float PAGE_MARGIN_RIGHT = 30f;
    public static final float PAGE_MARGIN_BOTTOM = 30f;


    public static final float BOX_HEADER_HEIGHT = 20f;
    public static final float BOX_HEADER_FONT_SIZE = 10f;

    public static final PDFont BOX_HEADER_FONT =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    public static final PDFont BOX_BODY_FONT =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    public static final float BOX_FONT_SIZE = 8f;
    public static final float TITLE_PAGE_PADDING = 10f;


    public static final float PAPER_MARGIN_TOP = 30f;
}
