package cc.kertaskerja.cetakservice.pdf;

import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinPemda;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinPemdaCetakResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PokinPemdaPDFGenerator {
    private static final float BOX_WIDTH = 220f;
    private static final float BOX_HEIGHT = 70f;
    private static final float BOX_HEADER_HEIGHT = 20f;

    private static final PDFont BOX_HEADER_FONT =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

    private static final PDFont BOX_BODY_FONT =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA);

    private static final float CONNECTOR_LENGTH = 30f;
    private static final float CONNECTOR_GAP = 20f;
    private static final float INLINE_GAP = 40f;
    private static final float VERTICAL_LINE_LENGTH = 30f;


    public byte[] generatePDF(PokinPemdaCetakResponse response) {

        PokinPemda root = response.item();

        try (
                // berawal dari document
                PDDocument document = new PDDocument();
                ByteArrayOutputStream output = new ByteArrayOutputStream()
        ) {

            // tambah page
            PDPage page = new PDPage(PDRectangle.A3);
            document.addPage(page);

            try (PDPageContentStream content =
                         new PDPageContentStream(document, page)) {

                PDRectangle pageSize = page.getMediaBox();
                float pageWidth = pageSize.getWidth();
                float pageHeight = pageSize.getHeight();

                // judul
                drawTitle(content, page, "POHON KINERJA " + response.nama());

                // root (tema)
                float xRoot = (pageWidth - BOX_WIDTH) / 2;
                float yRoot = pageHeight - 200;

                NodePosition rootPos = new NodePosition(
                        xRoot,
                        yRoot,
                        BOX_WIDTH,
                        BOX_HEIGHT
                );

                drawKotakPokin(content, rootPos, root);
                // childs
                if (!root.childs().isEmpty()) {
                    drawVerticalLine(content,
                            rootPos.centerX(),
                            rootPos.bottom());

                    // coba 2 child
                    int childCount = 2;
                    float totalWidth = childCount * BOX_WIDTH + (childCount - 1) * INLINE_GAP;
                    float childStartX = rootPos.centerX() - (totalWidth / 2);
                    float childY = rootPos.bottom() - CONNECTOR_LENGTH - BOX_HEIGHT - VERTICAL_LINE_LENGTH;

                    List<NodePosition> childPositions = new ArrayList<>();

                    for (int i = 0; i < childCount; i++) {
                        float x = childStartX + i * (BOX_WIDTH + INLINE_GAP);

                        NodePosition pos = new NodePosition(
                                x,
                                childY,
                                BOX_WIDTH,
                                BOX_HEIGHT
                        );

                        childPositions.add(pos);

                        drawKotakPokin(content, pos, root.childs().get(i));
                    }

                    // first child
                    NodePosition firstChildPos = childPositions.getFirst();
                    // last child
                    NodePosition lastChildPos = childPositions.getLast();

                    float horizontalY = rootPos.bottom() - CONNECTOR_LENGTH;

                    drawHorizontalLine(content,
                            firstChildPos.centerX(),
                            lastChildPos.centerX(),
                            horizontalY
                    );

                    for (NodePosition childPos : childPositions) {
                        drawVerticalLine(
                                content,
                                childPos.centerX(),
                                horizontalY
                        );
                    }
                }
            }

            document.save(output);

            return output.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed generate PDF", e);
        }
    }

    private void drawTitle(
            PDPageContentStream content,
            PDPage page,
            String title
    ) throws IOException {

        PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        float fontSize = 20f;
        float topMargin = 30f;

        PDRectangle pageSize = page.getMediaBox();

        float pageWidth = pageSize.getWidth();
        float pageHeight = pageSize.getHeight();

        float titleWidth = font.getStringWidth(title) / 1000 * fontSize;
        float x = (pageWidth - titleWidth) / 2;
        float y = pageHeight - topMargin;

        content.beginText();
        content.setFont(font, fontSize);
        content.newLineAtOffset(x, y);
        content.showText(title);
        content.endText();
    }

    private void drawVerticalLine(
            PDPageContentStream content,
            float x,
            float y
    ) throws IOException {
        content.moveTo(x, y);
        content.lineTo(x, y - VERTICAL_LINE_LENGTH);
        content.stroke();
    }

    private void drawHorizontalLine(
            PDPageContentStream content,
            float startX,
            float endX,
            float y
    ) throws IOException {

        content.moveTo(startX, y);
        content.lineTo(endX, y);
        content.stroke();
    }

    private void drawKotakPokin(
            PDPageContentStream content,
            NodePosition pos,
            PokinPemda pokin
    ) throws IOException {

        final float width = 220f;
        final float height = 70f;
        final float headerHeight = 20f;

        // Border luar
        content.addRect(pos.x(), pos.y(), width, height);

        // Garis pembatas
        content.moveTo(pos.x(), pos.y() + height - headerHeight);
        content.lineTo(pos.x() + width, pos.y() + height - headerHeight);

        content.stroke();

        drawCenteredText(
                content,
                pokin.jenisPohon(),
                pos.x(),
                pos.y() + height - headerHeight,
                width,
                headerHeight,
                BOX_HEADER_FONT,
                10f
        );

        drawCenteredText(
                content,
                pokin.namaPohon(),
                pos.x(),
                pos.y(),
                width,
                height - headerHeight,
                BOX_BODY_FONT,
                10f
        );
    }

    private void drawCenteredText(
            PDPageContentStream content,
            String text,
            float x,
            float y,
            float width,
            float height,
            PDFont font,
            float fontSize
    ) throws IOException {

        float textWidth = font.getStringWidth(text) / 1000 * fontSize;

        float textX = x + (width - textWidth) / 2;

        // Perkiraan posisi vertikal agar tampak di tengah
        float textY = y + (height - fontSize) / 2 + (fontSize * 0.3f);

        content.beginText();
        content.setFont(font, fontSize);
        content.newLineAtOffset(textX, textY);
        content.showText(text);
        content.endText();
    }
}
