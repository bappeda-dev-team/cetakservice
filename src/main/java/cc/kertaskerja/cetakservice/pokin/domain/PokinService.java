package cc.kertaskerja.cetakservice.pokin.domain;

import cc.kertaskerja.cetakservice.client.perencanaan.PerencanaanClient;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinOpdCetakResponse;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinPemdaCetakResponse;
import cc.kertaskerja.cetakservice.pdf.PokinOpdPDFGenerator;
import cc.kertaskerja.cetakservice.pdf.PokinPemdaPDFGenerator;
import cc.kertaskerja.cetakservice.pdf.pokin.Node;
import cc.kertaskerja.cetakservice.storage.PdfStorageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PokinService {

    private final PerencanaanClient perencanaanClient;
    private final PokinPemdaPDFGenerator pokinPemdaPDFGenerator;
    private final PokinOpdPDFGenerator pokinOpdPDFGenerator;
    private final PdfStorageService pdfStorageService;

    private final TreeBuilder treeBuilder;

    public PokinService(
            PerencanaanClient perencanaanClient,
            PokinPemdaPDFGenerator pokinPemdaPDFGenerator,
            PokinOpdPDFGenerator pokinOpdPDFGenerator,
            PdfStorageService pdfStorageService,
            TreeBuilder treeBuilder
            ) {
        this.perencanaanClient = perencanaanClient;
        this.pokinPemdaPDFGenerator = pokinPemdaPDFGenerator;
        this.pokinOpdPDFGenerator = pokinOpdPDFGenerator;
        this.pdfStorageService = pdfStorageService;
        this.treeBuilder = treeBuilder;
    }

    public String cetakPokinPemda(Integer pokinId) {

        PokinPemdaCetakResponse response =
                perencanaanClient.getPokinPemdaCetak(pokinId);

        String version = response.version();
        String key = "pokin/pemda/%d/%s".formatted(pokinId, version);
        // cek ke upload service apakah key tersebut sudah ada
        Optional<String> existing = pdfStorageService.findPdf(key);

        if (existing.isPresent()) {
            return existing.get();
        }

        List<Node> pokinTree = treeBuilder.build(response.item());

        byte[] pdf =
                pokinPemdaPDFGenerator.generatePDF(pokinTree);

        return pdfStorageService.storePdf(
                pdf,
                "pokin-pemda-%d-%s.pdf".formatted(pokinId, version),
                "pokin-pemda",
                key
                );
    }

    public String cetakPokinOpd(String kodeOpd, Integer tahun) {
        PokinOpdCetakResponse response =
                perencanaanClient.getPokinOpdCetak(kodeOpd, tahun);

        String version = response.version();
        String key = "pokin/opd/%s/%d/%s".formatted(kodeOpd, tahun, version);
        // cek ke upload service apakah key tersebut sudah ada
        Optional<String> existing = pdfStorageService.findPdf(key);

        if (existing.isPresent()) {
            return existing.get();
        }

        Node pokinTree = treeBuilder.buildPokinOpd(response.item());
        byte[] pdf =
                pokinOpdPDFGenerator.generatePDF(pokinTree);

        return pdfStorageService.storePdf(
                pdf,
                "pokin-opd-%s-%d-%s.pdf".formatted(kodeOpd, tahun, version),
                "pokin-opd",
                key
        );
    }
}
