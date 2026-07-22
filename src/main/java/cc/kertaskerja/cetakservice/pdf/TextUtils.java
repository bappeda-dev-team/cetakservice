package cc.kertaskerja.cetakservice.pdf;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class TextUtils {

    private TextUtils() {
    }

    public static void drawCenteredText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            float width,
            float height,
            PDFont font,
            float fontSize) throws IOException {
        drawCenteredText(content, text, x, y, width, height, font, fontSize, Color.BLACK);
    }

    public static void drawCenteredText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            float width,
            float height,
            PDFont font,
            float fontSize,
            Color textColor) throws IOException {

        float textWidth = font.getStringWidth(text) / 1000 * fontSize;

        float textX = x + (width - textWidth) / 2;

        // Perkiraan posisi vertikal agar tampak di tengah
        float textY = y + (height - fontSize) / 2 + (fontSize * 0.3f);

        content.beginText();
        content.setNonStrokingColor(textColor);
        content.setFont(font, fontSize);
        content.newLineAtOffset(textX, textY);
        content.showText(text);
        content.endText();
        content.setNonStrokingColor(Color.BLACK);
    }

    public static void drawCenteredMultilineText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            float width,
            float height,
            PDFont font,
            float fontSize) throws IOException {

        final float padding = 6f;
        final float lineHeight = fontSize * 1.2f;

        List<String> lines = wrapText(
                text,
                font,
                fontSize,
                width - (padding * 2));

        // Berapa baris yang benar-benar muat di box
        int maxLines = Math.max(1, (int) Math.floor((height - padding * 2) / lineHeight));

        if (lines.size() > maxLines) {
            lines = new ArrayList<>(lines.subList(0, maxLines));

            String last = lines.get(maxLines - 1);

            while (!last.isEmpty()) {
                String candidate = last + "...";

                float candidateWidth = font.getStringWidth(candidate) / 1000f * fontSize;

                if (candidateWidth <= width - padding * 2) {
                    last = candidate;
                    break;
                }

                last = last.substring(0, last.length() - 1);
            }

            lines.set(maxLines - 1, last);
        }

        // float totalHeight = lines.size() * lineHeight;

        float currentY = y + height - padding - fontSize;

        for (String line : lines) {

            float textWidth = font.getStringWidth(line) / 1000f * fontSize;

            float textX = x + (width - textWidth) / 2;

            content.beginText();
            content.setFont(font, fontSize);
            content.newLineAtOffset(textX, currentY);
            content.showText(line);
            content.endText();

            currentY -= lineHeight;
        }
    }

    public static List<String> wrapText(
            String text,
            PDFont font,
            float fontSize,
            float maxWidth) throws IOException {

        List<String> lines = new ArrayList<>();

        if (text == null || text.isBlank()) {
            return lines;
        }

        for (String sourceLine : text.split("\\R", -1)) {

            if (sourceLine.isBlank()) {
                lines.add("");
                continue;
            }

            StringBuilder currentLine = new StringBuilder();

            for (String word : sourceLine.trim().split("\\s+")) {
                String candidate = currentLine.isEmpty()
                        ? word
                        : currentLine + " " + word;

                float width = font.getStringWidth(candidate) / 1000f * fontSize;

                if (width <= maxWidth) {
                    currentLine.setLength(0);
                    currentLine.append(candidate);
                } else {
                    if (!currentLine.isEmpty()) {
                        lines.add(currentLine.toString());
                    }

                    currentLine.setLength(0);
                    currentLine.append(word);
                }
            }

            if (!currentLine.isEmpty()) {
                lines.add(currentLine.toString());
            }
        }

        return lines;
    }

    public static void drawJudulHalaman(
            PDPageContentStream content,
            float x,
            float y,
            String judulHalaman) throws IOException {
        // default, change later if needed
        PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        float fontSize = 18f;

        String judulBener = judulHalaman.replace("\t", " ");

        content.beginText();
        content.setFont(font, fontSize);
        content.newLineAtOffset(x, y);
        content.showText(judulBener);
        content.endText();
    }
}
