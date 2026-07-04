package cc.kertaskerja.cetakservice.pdf;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

public final class ShapeUtils {

    private ShapeUtils() {}


    public static void drawVerticalLine(
            PDPageContentStream content,
            float x,
            float y,
            float lineLength
    ) throws IOException {
        content.moveTo(x, y);
        content.lineTo(x, y - lineLength);
        content.stroke();
    }

    public static void drawHorizontalLine(
            PDPageContentStream content,
            float startX,
            float endX,
            float y
    ) throws IOException {

        content.moveTo(startX, y);
        content.lineTo(endX, y);
        content.stroke();
    }
}
