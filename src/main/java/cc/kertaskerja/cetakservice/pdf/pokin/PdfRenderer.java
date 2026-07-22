package cc.kertaskerja.cetakservice.pdf.pokin;

import cc.kertaskerja.cetakservice.pdf.NodePosition;
import cc.kertaskerja.cetakservice.pdf.ShapeUtils;
import cc.kertaskerja.cetakservice.pdf.TextUtils;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.*;

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

                content.beginText();
                content.setFont(font, fontSize);
                content.newLineAtOffset(x, y);
                content.showText(title);
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
                                - getNodeSize(parent.getNode()).height()
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

        private static final float CROSSCUTTING_BOX_HEIGHT = 150f;
        private static final float CROSSCUTTING_TITLE_HEIGHT = 40f;

        private static final float CROSSCUTTING_LABEL_HEIGHT = 12f;

        private void drawCrosscutting(
                        PDPageContentStream content,
                        float x,
                        float y,
                        float width,
                        float height,
                        Node node) throws IOException {

                float namaHeight = height * 0.40f;
                float labelHeight = CROSSCUTTING_LABEL_HEIGHT;
                float targetHeight = height - namaHeight - labelHeight;

                // Bagian bawah: isi crosscutting
                float targetY = y;
                TextUtils.drawCenteredMultilineText(
                                content,
                                buildCrosscuttingText(node),
                                x,
                                targetY,
                                width,
                                targetHeight,
                                BOX_BODY_FONT,
                                BOX_FONT_SIZE - 1);

                // Tengah: label CROSSCUTTING
                float labelY = targetY + targetHeight;
                ShapeUtils.drawHorizontalLine(content, x, x + width, labelY);

                TextUtils.drawCenteredText(
                                content,
                                "CROSSCUTTING",
                                x,
                                labelY,
                                width,
                                labelHeight,
                                BOX_BODY_FONT,
                                BOX_FONT_SIZE - 2);

                // Bagian atas: nama pohon asli
                float namaY = labelY + labelHeight;
                ShapeUtils.drawHorizontalLine(content, x, x + width, namaY);

                drawNamaPokin(
                                content,
                                x,
                                namaY,
                                width,
                                namaHeight,
                                node.namaPohon());

                ShapeUtils.drawHorizontalLine(
                                content,
                                x,
                                x + width,
                                labelY);
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

                        TextUtils.drawCenteredMultilineText(
                                        content,
                                        (i + 1) + ". " + tujuan.namaTujuan(),
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

                TextUtils.drawCenteredMultilineText(
                                content,
                                namaPohon.trim(),
                                x,
                                y,
                                width,
                                height,
                                BOX_BODY_FONT,
                                BOX_FONT_SIZE);
        }

        private String buildCrosscuttingText(Node node) {

                return node.nodeMetadata()
                                .crosscuttingPokins()
                                .stream()
                                .map(cp -> """
                                                Nama Pohon:
                                                %s

                                                OPD Penerima:
                                                %s
                                                """.formatted(
                                                cp.namaPohonPenerima(),
                                                cp.namaOpdPenerima()))
                                .collect(Collectors.joining("\n\n"));
        }

        private final float TUJUAN_OPD_BOX_HEIGHT = 80f;

        private NodeSize getNodeSize(Node node) {
                if (node.nodeMetadata().isCrosscutting()) {
                        return new NodeSize(
                                        BOX_WIDTH,
                                        CROSSCUTTING_BOX_HEIGHT);
                }

                if (hasTujuanOpd(node)) {
                        return new NodeSize(
                                        BOX_WIDTH,
                                        TUJUAN_OPD_BOX_HEIGHT);
                }

                return new NodeSize(
                                BOX_WIDTH,
                                BOX_HEIGHT);
        }
}
