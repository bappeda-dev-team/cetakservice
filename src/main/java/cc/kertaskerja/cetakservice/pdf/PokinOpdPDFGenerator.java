package cc.kertaskerja.cetakservice.pdf;

import cc.kertaskerja.cetakservice.pdf.pokin.*;
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
import java.util.List;

@Service
public class PokinOpdPDFGenerator {
    private final LayoutEngine layoutEngine;
    private final PdfRenderer pdfRenderer;
    private final ViewGenerator viewGenerator;
    private final RenderTreeBuilder renderTreeBuilder;

    public PokinOpdPDFGenerator(LayoutEngine layoutEngine, PdfRenderer pdfRenderer, ViewGenerator viewGenerator, RenderTreeBuilder renderTreeBuilder) {
        this.layoutEngine = layoutEngine;
        this.pdfRenderer = pdfRenderer;
        this.viewGenerator = viewGenerator;
        this.renderTreeBuilder = renderTreeBuilder;
    }

    public byte[] generatePDF(Node root) {

        try (
                // berawal dari document
                PDDocument document = new PDDocument();
                ByteArrayOutputStream output = new ByteArrayOutputStream()
        ) {

            // tulis halaman judul di method ini
            drawTitlePage(document, "POHON KINERJA OPD");

            List<PagePlan> plans = viewGenerator.generate(root);

            for (PagePlan pagePlan : plans) {
                PDPage page = new PDPage(PageOrientation.LANDSCAPE.createRectangle(PDRectangle.A0));
                document.addPage(page);

                Node renderTree = renderTreeBuilder.build(pagePlan);
                LayoutResult layout = layoutEngine.layout(renderTree);

                try (PDPageContentStream content =
                             new PDPageContentStream(document, page)) {
                    RenderPage renderPage = new RenderPage(
                            pagePlan.current().jenisPohon().getLabel(),
                            pagePlan.current().namaPohon(),
                            layout
                    );
                    pdfRenderer.render(page, content, renderPage);
                }
            }

            document.save(output);

            return output.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed generate PDF", e);
        }
    }

    private void drawTitlePage(
            PDDocument document,
            String judulBuku
    ) throws IOException {

        PDRectangle pageSize = PageOrientation.LANDSCAPE.createRectangle(PDRectangle.A4);

        PDPage page = new PDPage(pageSize);
        document.addPage(page);


        try (PDPageContentStream content = new PDPageContentStream(document, page)) {
            PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            float fontSize = 20f;
            float topMargin = 30f;

            float pageWidth = pageSize.getWidth();
            float pageHeight = pageSize.getHeight();

            float titleWidth = font.getStringWidth(judulBuku) / 1000 * fontSize;
            float x = (pageWidth - titleWidth) / 2;
            float y = pageHeight - topMargin;

            content.beginText();
            content.setFont(font, fontSize);
            content.newLineAtOffset(x, y);
            content.showText(judulBuku);
            content.endText();
        }
    }
}
