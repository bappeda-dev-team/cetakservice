package cc.kertaskerja.cetakservice.pdf.pokin;

import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.BOX_BODY_FONT;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.BOX_FONT_SIZE;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.BOX_HEADER_FONT;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.BOX_HEADER_FONT_SIZE;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.BOX_HEADER_HEIGHT;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.BOX_HEIGHT;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.BOX_WIDTH;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.PAGE_HEADER_HEIGHT;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.PAGE_HEADER_SPACING;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.PAGE_MARGIN_BOTTOM;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.PAGE_MARGIN_LEFT;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.PAGE_MARGIN_RIGHT;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.PAGE_MARGIN_TOP;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.PAPER_MARGIN_TOP;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.TITLE_PAGE_PADDING;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;

import cc.kertaskerja.cetakservice.pdf.ShapeUtils;
import cc.kertaskerja.cetakservice.pdf.TextUtils;

@Component
public class PdfRenderer {
    public void render(
            PDPage page,
            PDPageContentStream content,
            RenderPage renderPage) throws IOException {

        drawPageTitle(content, page, renderPage);
        // jarak antara title dengan pohon
        float contentStartY = PAGE_MARGIN_TOP +
                PAGE_HEADER_HEIGHT +
                TITLE_PAGE_PADDING +
                PAPER_MARGIN_TOP;

        float visualWidth = renderPage.layout().bound().width() + BOX_WIDTH;
        float availableWidth = page.getMediaBox().getWidth()
                - PAGE_MARGIN_LEFT
                - PAGE_MARGIN_RIGHT;
        // biar pohon center
        float contentOffsetX = PAGE_MARGIN_LEFT + (availableWidth - visualWidth) / 2f
                - renderPage.layout().bound().minX();

        ContentArea contentArea = getContentArea(page);

        drawContentBorder(content, contentArea);

        drawTree(content, page, renderPage.layout().root(), contentOffsetX, contentStartY - 5f);
    }

    public void renderCover(
            PDPage page,
            PDPageContentStream content,
            RenderCover renderCover) throws IOException {

        // judul cover
        title(page, content, renderCover.title());

        // jarak antara title dengan pohon
        float contentStartY = PAGE_MARGIN_TOP +
                PAGE_HEADER_HEIGHT +
                TITLE_PAGE_PADDING +
                PAPER_MARGIN_TOP;

        float availableWidth = page.getMediaBox().getWidth()
                - PAGE_MARGIN_LEFT
                - PAGE_MARGIN_RIGHT;
        // biar pohon center
        float treeLeft = renderCover.layout().bound().minX() - BOX_WIDTH / 2f;
        float treeRight = renderCover.layout().bound().maxX() + BOX_WIDTH / 2f;
        float visualWidth = treeRight - treeLeft;

        float contentOffsetX = PAGE_MARGIN_LEFT
                + (availableWidth - visualWidth) / 2f
                - treeLeft;

        ContentArea contentArea = getContentArea(page);

        drawContentBorder(content, contentArea);
        drawTree(content, page, renderCover.layout().root(), contentOffsetX, contentStartY - 5f);
    }

    private void title(
            PDPage page,
            PDPageContentStream content,
            String title) throws IOException {

        PDRectangle mediaBox = page.getMediaBox();

        PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

        float fontSize = 24f;

        /*
         * kalau mau center
         * float titleWidth = font.getStringWidth(title) / 1000f * fontSize;
         * float x = (mediaBox.getWidth() - titleWidth) / 2f;
         */
        float x = 20f;

        float y = mediaBox.getHeight() - PAPER_MARGIN_TOP;

        String sanitizedText = sanitize(title);

        content.beginText();
        content.setFont(font, fontSize);
        content.newLineAtOffset(x, y);
        content.showText(sanitizedText);
        content.endText();
    }

    private ContentArea getContentArea(PDPage page) {
        float width = page.getMediaBox().getWidth()
                - PAGE_MARGIN_LEFT
                - PAGE_MARGIN_RIGHT;

        float height = page.getMediaBox().getHeight()
                - PAGE_MARGIN_TOP
                - PAGE_MARGIN_BOTTOM
                - PAGE_HEADER_HEIGHT
                - PAGE_HEADER_SPACING;

        return new ContentArea(PAGE_MARGIN_LEFT, PAGE_MARGIN_BOTTOM, width, height);
    }

    private void drawContentBorder(
            PDPageContentStream content,
            ContentArea contentArea) throws IOException {
        content.addRect(
                contentArea.x(),
                contentArea.y(),
                contentArea.width(),
                contentArea.height());

        content.stroke();
    }

    private void drawPageTitle(
            PDPageContentStream content,
            PDPage page,
            RenderPage renderPage) throws IOException {
        float x = 20f;
        float y = page.getMediaBox().getHeight() - PAPER_MARGIN_TOP;
        String titleText = renderPage.judulHalaman();
        TextUtils.drawJudulHalaman(content, x, y, titleText);
    }

    private void drawTree(
            PDPageContentStream content,
            PDPage page,
            LayoutNode parent,
            float contentOffsetX,
            float contentStartY) throws IOException {

        drawNode(content, page, parent, contentOffsetX, contentStartY);

        drawConnectors(content, page, parent, contentOffsetX, contentStartY);

        for (LayoutNode child : parent.getChildren()) {
            drawTree(content, page, child, contentOffsetX, contentStartY);
        }
    }

    private static final float CONNECTOR_GAP = 30f;
    private static final float CHILD_CONNECTOR_GAP = 10f;

    private void drawConnectors(
            PDPageContentStream content,
            PDPage page,
            LayoutNode parent,
            float contentOffsetX,
            float contentStartY) throws IOException {

        List<LayoutNode> children = parent.getChildren();
        // no children = no connector
        if (children.isEmpty()) {
            return;
        }

        float pageHeight = page.getMediaBox().getHeight();

        // posisi x di tengah kotak parent
        float parentCenterX = parent.getX() + contentOffsetX;

        // posisi y dibawah kotak parent
        float parentBottomY = pageHeight
                - parent.getY()
                - NodeSize.from(parent.getNode()).height()
                - contentStartY;

        // jika child hanya 1, vertical connector saja
        if (children.size() == 1) {

            LayoutNode child = children.getFirst();

            // posisi y diatas kotak child
            float childTopY = pageHeight - child.getY() - contentStartY;

            ShapeUtils.drawVerticalLine(
                    content,
                    parentCenterX,
                    parentBottomY,
                    childTopY);

            return;
        }

        // ==========================================
        // Kasus lebih dari satu child
        // ==========================================

        // garis menuju horizontal line
        float busY = parentBottomY - 10f;

        // parent -> bus
        ShapeUtils.drawVerticalLine(
                content,
                parentCenterX,
                parentBottomY,
                busY);

        // horizontal bus
        float startX = children.getFirst().getX() + contentOffsetX;

        float endX = children.getLast().getX() + contentOffsetX;

        ShapeUtils.drawHorizontalLine(
                content,
                startX,
                endX,
                busY);

        // bus -> child
        for (LayoutNode child : children) {

            float childCenterX = child.getX() + contentOffsetX;

            float childTopY = pageHeight - child.getY() - contentStartY;

            ShapeUtils.drawVerticalLine(
                    content,
                    childCenterX,
                    busY,
                    childTopY);
        }
    }

    private static final float BODY_TOP_PADDING = 4f;

    private void drawNode(
            PDPageContentStream content,
            PDPage page,
            LayoutNode node,
            float contentOffsetX,
            float contentStartY) throws IOException {

        float pageHeight = page.getMediaBox().getHeight();

        Node data = node.getNode();

        NodeSize size = getNodeSize(data);

        float x = node.getX() + contentOffsetX - size.width() / 2f;
        float y = pageHeight - node.getY() - size.height() - contentStartY;

        drawKotak(
                content,
                x, y,
                size.width(), size.height());

        // jenis pokin
        drawHeader(
                content,
                x,
                y,
                size.width(),
                size.height(),
                node);

        // garis pemisah title dan body
        drawBorder(
                content,
                x,
                y,
                size.width(),
                size.height());

        // nama pokin
        float bodyY = y;
        float bodyHeight = size.height() - BOX_HEADER_HEIGHT;

        drawBody(
                content,
                x,
                bodyY,
                size.width(),
                bodyHeight,
                node);
    }

    private void drawKotak(
            PDPageContentStream content,
            float x,
            float y,
            float width,
            float height) throws IOException {

        // Border luar
        content.addRect(
                x,
                y,
                width,
                height);

        content.stroke();
    }

    private void drawHeader(
            PDPageContentStream content,
            float x,
            float y,
            float width,
            float height,
            LayoutNode node) throws IOException {

        final float HEADER_PADDING = 20f;
        float headerY = y + height - BOX_HEADER_HEIGHT;

        // background header berwarna sesuai level
        ShapeUtils.drawFilledRect(
                content,
                x,
                headerY,
                width,
                BOX_HEADER_HEIGHT,
                node.getNode().jenisPohon().getHeaderColor());

        // judul pokin: "LABEL id" (mengikuti tampilan web, mis. "Startegic 665")
        TextUtils.drawCenteredText(
                content,
                headerTitle(node),
                x,
                headerY + HEADER_PADDING,
                width,
                BOX_HEADER_HEIGHT - HEADER_PADDING * 2,
                BOX_HEADER_FONT,
                BOX_HEADER_FONT_SIZE,
                node.getNode().jenisPohon().getTextColor());
    }

    private String headerTitle(LayoutNode node) {
        String label = node.getNode().jenisPohon().getLabel();
        Integer id = node.getNode().nodeMetadata().nomor();
        Integer level = node.getNode().levelPohon();
        // id root OPD di-set -1 (bukan id nyata), jadi jangan tampilkan
        if (id == null || id < 0 || level == 0) {
            return label;
        }
        return label + " " + id;
    }

    private void drawBorder(
            PDPageContentStream content,
            float x,
            float y,
            float width,
            float height) throws IOException {
        // content.addRect(x, y, width, BOX_HEIGHT);
        // content.stroke();
        ShapeUtils.drawHorizontalLine(
                content,
                x,
                x + width,
                y + height - BOX_HEADER_HEIGHT);
    }

    private void drawBody(
            PDPageContentStream content,
            float x,
            float y,
            float width,
            float height,
            LayoutNode node) throws IOException {

        Node data = node.getNode();

        if (hasTujuanOpd(data)) {
            drawTujuanOpd(
                    content,
                    x,
                    y,
                    width,
                    height,
                    data.nodeMetadata().tujuanOpds());
            return;
        }

        if (hasCrosscutting(data)) {
            drawCrosscutting(
                    content,
                    x,
                    y,
                    width,
                    height,
                    data);
            return;
        }

        drawNamaPokin(
                content,
                x,
                y,
                width,
                height,
                data.namaPohon());
    }

    private static final float CROSSCUTTING_PADDING = 6f;

    private void drawCrosscutting(
            PDPageContentStream content,
            float x,
            float y,
            float width,
            float height,
            Node node) throws IOException {

        float dividerY = y + height / 2f;

        // =====================
        // AREA ATAS (PEMBERI)
        // =====================
        float topY = dividerY;
        float topHeight = y + height - dividerY;

        TextUtils.drawCenteredMultilineText(
                content,
                buildCrosscuttingPemberi(node),
                x,
                topY,
                width,
                topHeight,
                BOX_BODY_FONT,
                BOX_FONT_SIZE - 1);

        // =====================
        // GARIS PEMISAH
        // =====================
        ShapeUtils.drawHorizontalLine(
                content,
                x,
                x + width,
                dividerY);

        // =====================
        // AREA BAWAH (PENERIMA)
        // =====================
        float bottomY = y;
        float bottomHeight = dividerY - y;

        TextUtils.drawCenteredMultilineText(
                content,
                buildCrosscuttingPenerima(node),
                x,
                bottomY,
                width,
                bottomHeight,
                BOX_BODY_FONT,
                BOX_FONT_SIZE - 1);
    }

    private boolean hasCrosscutting(Node node) {
        return node.nodeMetadata() != null &&
                node.nodeMetadata().isCrosscutting() &&
                !node.nodeMetadata().crosscuttingPokins().isEmpty();
    }

    private boolean hasTujuanOpd(Node node) {
        return node.nodeMetadata() != null &&
                node.nodeMetadata().tujuanOpds() != null &&
                !node.nodeMetadata().tujuanOpds().isEmpty();
    }

    private void drawTujuanOpd(
            PDPageContentStream content,
            float x,
            float y,
            float width,
            float height,
            List<TujuanOpd> tujuanOpds) throws IOException {

        if (tujuanOpds.isEmpty()) {
            return;
        }

        float itemHeight = height / tujuanOpds.size();

        float currentY = y + height - itemHeight;

        for (int i = 0; i < tujuanOpds.size(); i++) {

            TujuanOpd tujuan = tujuanOpds.get(i);

            String sanitizedText = sanitize(tujuan.namaTujuan());

            TextUtils.drawCenteredMultilineText(
                    content,
                    (i + 1) + ". " + sanitizedText,
                    x,
                    currentY,
                    width,
                    itemHeight,
                    BOX_BODY_FONT,
                    BOX_FONT_SIZE);

            currentY -= itemHeight;

            // // Garis pemisah antar tujuan (kecuali yang terakhir)
            // if (i < tujuanOpds.size() - 1) {
            // ShapeUtils.drawHorizontalLine(
            // content,
            // x,
            // x + width,
            // currentY
            // );
            // }
        }
    }

    private void drawNamaPokin(
            PDPageContentStream content,
            float x,
            float y,
            float width,
            float height,
            String namaPohon) throws IOException {

        if (namaPohon == null || namaPohon.isBlank()) {
            return;
        }

        String sanitizedText = sanitize(namaPohon);

        TextUtils.drawCenteredMultilineText(
                content,
                sanitizedText,
                x,
                y,
                width,
                height,
                BOX_BODY_FONT,
                BOX_FONT_SIZE);
    }

    private String buildCrosscuttingPemberi(Node node) {

        return node.nodeMetadata()
                .crosscuttingPokins()
                .stream()
                .map(cp -> """
                        %s
                        %s
                        %s
                        %s
                        """.formatted(
                        "--OPD-ASAL--",
                        cp.namaOpdPemberi(),
                        "--NAMA-POHON-ASAL--",
                        cp.namaPohonPemberi()))
                .collect(Collectors.joining("\n\n\n\n"));
    }

    private String buildCrosscuttingPenerima(Node node) {

        return node.nodeMetadata()
                .crosscuttingPokins()
                .stream()
                .map(cp -> """
                        %s
                        %s
                        %s
                        %s
                        %s
                        %s
                        %s
                        %s
                        """.formatted(
                        "--STATUS--",
                        cp.statusCrosscutting(),
                        "--OPD-PENERIMA--",
                        cp.namaOpdPenerima(),
                        "--NAMA-POHON-PENERIMA--",
                        cp.namaPohonPenerima(),
                        "--KETERANGAN--",
                        cp.keteranganCrosscutting()))
                .collect(Collectors.joining("\n\n\n\n\n\n\n\n"));
    }

    private NodeSize getNodeSize(Node node) {
        return NodeSize.from(node);
    }

    private String sanitize(String text) {
        return text
                .replace('\u2010', '-')
                .replace('\u2011', '-')
                .replace('\u2012', '-')
                .replace('\u2013', '-')
                .replace('\u2014', '-')
                .replace('\u00A0', ' ')
                .trim();
    }
}
