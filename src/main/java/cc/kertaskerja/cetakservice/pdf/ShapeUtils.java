package cc.kertaskerja.cetakservice.pdf;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

public final class ShapeUtils {

    private ShapeUtils() {}


    public static void drawVerticalLine(
            PDPageContentStream content,
            float x,
            float startY,
            float endY
    ) throws IOException {
        content.moveTo(x, startY);
        content.lineTo(x, endY);
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
