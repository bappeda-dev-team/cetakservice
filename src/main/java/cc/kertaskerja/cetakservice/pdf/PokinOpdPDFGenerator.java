package cc.kertaskerja.cetakservice.pdf;

import cc.kertaskerja.cetakservice.pdf.pokin.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
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

            // start cover page
            // tulis halaman judul di method ini
            // start cover page
            PDPage pageCover = new PDPage(PageOrientation.LANDSCAPE.createRectangle(PDRectangle.A1));
            document.addPage(pageCover);
            // tree for cover
            Node coverTree = renderTreeBuilder.buildCover(root);
            LayoutResult layoutCover = layoutEngine.layout(coverTree);
            RenderCover cover = new RenderCover(
                    "POHON KINERJA OPD",
                    layoutCover
            );

            try (PDPageContentStream content =
                         new PDPageContentStream(document, pageCover)) {
                pdfRenderer.renderCover(pageCover, content, cover);
            }
            // end cover page

            List<PagePlan> plans = viewGenerator.generate(root, ViewMode.OPD);

            for (PagePlan pagePlan : plans) {
                PDPage page = new PDPage(PageOrientation.LANDSCAPE.createRectangle(PDRectangle.A0));
                document.addPage(page);

                RenderTree renderTree = renderTreeBuilder.build(pagePlan);
                LayoutResult layout = layoutEngine.layout(renderTree.root());

                String judulHalaman =
                        "%s %d - %s".formatted(
                                renderTree.current().jenisPohon().getLabel(),
                                pagePlan.sequence(),
                                renderTree.current().namaPohon()
                        );

                RenderPage renderPage = new RenderPage(
                        judulHalaman,
                        renderTree.current().jenisPohon().getLabel(),
                        renderTree.current().namaPohon(),
                        layout
                );

                try (PDPageContentStream content =
                             new PDPageContentStream(document, page)) {
                    pdfRenderer.render(page, content, renderPage);
                }
            }

            document.save(output);

            return output.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed generate PDF", e);
        }
    }
}
