package cc.kertaskerja.cetakservice.pdf;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.Color;
import java.io.IOException;

public final class ShapeUtils {

    private ShapeUtils() {
    }

    /**
     * Gambar rectangle dengan warna isi (fill). Warna non-stroking dikembalikan
     * ke hitam setelah menggambar agar tidak mempengaruhi teks/gambar berikutnya.
     */
    // public static void drawFilledRect(
    //         PDPageContentStream content,
    //         float x,
    //         float y,
    //         float width,
    //         float height,
    //         Color fill) throws IOException {
    //     content.setNonStrokingColor(fill);
    //     content.addRect(x, y, width, height);
    //     content.fill();
    //     content.setNonStrokingColor(Color.BLACK);
    // }

    public static void drawVerticalLine(
            PDPageContentStream content,
            float x,
            float startY,
            float endY) throws IOException {
        content.moveTo(x, startY);
        content.lineTo(x, endY);
        content.stroke();
    }

    public static void drawHorizontalLine(
            PDPageContentStream content,
            float startX,
            float endX,
            float y) throws IOException {

        content.moveTo(startX, y);
        content.lineTo(endX, y);
        content.stroke();
    }

    public static void drawHorizontalGradientRect(
            PDPageContentStream content,
            float x, float y, float width, float height,
            Color start, Color end) throws IOException {
        int steps = 100;

        for (int i = 0; i < steps; i++) {
            float ratio = i / (float) (steps - 1);

            int red = (int) (start.getRed() + ratio * (end.getRed() - start.getRed()));
            int green = (int) (start.getGreen() + ratio * (end.getGreen() - start.getGreen()));
            int blue = (int) (start.getBlue() + ratio * (end.getBlue() - start.getBlue()));

            content.setNonStrokingColor(new Color(red, green, blue));
            content.addRect(x + (i * width / steps), y, width / steps + 0.1f, height);
            content.fill();
        }

        content.setNonStrokingColor(Color.BLACK);
    }
}
