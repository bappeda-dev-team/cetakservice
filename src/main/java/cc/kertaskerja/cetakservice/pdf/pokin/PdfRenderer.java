package cc.kertaskerja.cetakservice.pdf.pokin;

import cc.kertaskerja.cetakservice.pdf.ShapeUtils;
import cc.kertaskerja.cetakservice.pdf.TextUtils;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.*;

@Component
public class PdfRenderer {
    public void render(
            PDPage page,
            PDPageContentStream content,
            RenderPage renderPage
    ) throws IOException {

        drawPageTitle(content, page, renderPage);
        // jarak antara title dengan pohon
        float contentStartY =
                PAGE_MARGIN_TOP +
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

    private ContentArea getContentArea(PDPage page) {
        float width =
                page.getMediaBox().getWidth()
                        - PAGE_MARGIN_LEFT
                        - PAGE_MARGIN_RIGHT;

        float height =
                page.getMediaBox().getHeight()
                        - PAGE_MARGIN_TOP
                        - PAGE_MARGIN_BOTTOM
                        - PAGE_HEADER_HEIGHT
                        - PAGE_HEADER_SPACING;

        return new ContentArea(PAGE_MARGIN_LEFT, PAGE_MARGIN_BOTTOM, width, height);
    }

    private void drawContentBorder(
            PDPageContentStream content,
            ContentArea contentArea
    ) throws IOException {
        content.addRect(
                contentArea.x(),
                contentArea.y(),
                contentArea.width(),
                contentArea.height()
        );

        content.stroke();
    }

    private void drawPageTitle(
            PDPageContentStream content,
            PDPage page,
            RenderPage renderPage
    ) throws IOException {
        float x = 20f;
        float y = page.getMediaBox().getHeight() - PAPER_MARGIN_TOP;
        String titleText = renderPage.title() + " - " + renderPage.subTitle();
        TextUtils.drawJudulHalaman(content, x, y, titleText);
    }

    private void drawTree(
            PDPageContentStream content,
            PDPage page,
            LayoutNode parent,
            float contentOffsetX,
            float contentStartY
    ) throws IOException {

        drawNode(content, page, parent, contentOffsetX, contentStartY);

        drawConnectors(content, page, parent,  contentOffsetX, contentStartY);

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
            float contentStartY
    ) throws IOException {

        List<LayoutNode> children = parent.getChildren();
        // no children = no connector
        if (children.isEmpty()) {
            return;
        }

        float pageHeight = page.getMediaBox().getHeight();

        // posisi x di tengah kotak parent
        float parentCenterX = parent.getX() + contentOffsetX;

        // posisi y dibawah kotak parent
        float parentBottomY = pageHeight - parent.getY() - BOX_HEIGHT - contentStartY;

        // jika child hanya 1, vertical connector saja
        if (children.size() == 1) {

            LayoutNode child = children.getFirst();

            // posisi y diatas kotak child
            float childTopY = pageHeight - child.getY() - contentStartY;

            ShapeUtils.drawVerticalLine(
                    content,
                    parentCenterX,
                    parentBottomY,
                    childTopY
            );

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
                busY
        );

        // horizontal bus
        float startX =
                children.getFirst().getX() + contentOffsetX;

        float endX =
                children.getLast().getX() + contentOffsetX;

        ShapeUtils.drawHorizontalLine(
                content,
                startX,
                endX,
                busY
        );

        // bus -> child
        for (LayoutNode child : children) {

            float childCenterX =
                    child.getX() + contentOffsetX;

            float childTopY =
                    pageHeight - child.getY() - contentStartY;

            ShapeUtils.drawVerticalLine(
                    content,
                    childCenterX,
                    busY,
                    childTopY
            );
        }
    }

    private void drawNode(
            PDPageContentStream content,
            PDPage page,
            LayoutNode node,
            float contentOffsetX,
            float contentStartY
    ) throws IOException {

        float pageHeight = page.getMediaBox().getHeight();

        float x = node.getX() + contentOffsetX - BOX_WIDTH / 2f;
        float y = pageHeight - node.getY() - BOX_HEIGHT - contentStartY;

        // jenis pokin
        drawHeader(content, x, y, node);

        // garis pemisah title dan body
        drawBorder(content, x, y);

        // nama pokin
        drawBody(content, x, y, node);
    }

    private void drawHeader(
            PDPageContentStream content,
            float x,
            float y,
            LayoutNode node
    ) throws IOException {
        // judul pokin
        TextUtils.drawCenteredText(
                content,
                node.getNode().jenisPohon().getLabel(),
                x,
                y + BOX_HEIGHT - BOX_HEADER_HEIGHT,
                BOX_WIDTH,
                BOX_HEADER_HEIGHT,
                BOX_HEADER_FONT,
                BOX_HEADER_FONT_SIZE
        );
    }

    private void drawBorder(
            PDPageContentStream content,
            float x,
            float y
    ) throws IOException {
        content.addRect(x, y, BOX_WIDTH, BOX_HEIGHT);
        content.stroke();
        ShapeUtils.drawHorizontalLine(content, x, x + BOX_WIDTH, y + BOX_HEIGHT - BOX_HEADER_HEIGHT);
    }

    private void drawBody(
            PDPageContentStream content,
            float x,
            float y,
            LayoutNode node
    ) throws IOException {
        // nama pokin
        TextUtils.drawCenteredMultilineText(
                content,
                node.getNode().namaPohon(),
                x,
                y,
                BOX_WIDTH,
                BOX_HEIGHT - BOX_HEADER_HEIGHT,
                BOX_BODY_FONT,
                BOX_FONT_SIZE
        );
    }
}
