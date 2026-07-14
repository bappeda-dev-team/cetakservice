package cc.kertaskerja.cetakservice.pokin.domain;

import cc.kertaskerja.cetakservice.client.perencanaan.PerencanaanClient;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinOpdCetakResponse;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinPemdaCetakResponse;
import cc.kertaskerja.cetakservice.client.upload.UploadClient;
import cc.kertaskerja.cetakservice.client.upload.domain.UploadRequest;
import cc.kertaskerja.cetakservice.common.LocalStorageService;
import cc.kertaskerja.cetakservice.pdf.PokinOpdPDFGenerator;
import cc.kertaskerja.cetakservice.pdf.PokinPemdaPDFGenerator;
import cc.kertaskerja.cetakservice.pdf.pokin.Node;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;

@Service
public class PokinService {

    private final PerencanaanClient perencanaanClient;
    private final UploadClient uploadClient;
    private final PokinPemdaPDFGenerator pokinPemdaPDFGenerator;
    private final PokinOpdPDFGenerator pokinOpdPDFGenerator;
    private final LocalStorageService localStorageService;

    private final TreeBuilder treeBuilder;

    public PokinService(
            PerencanaanClient perencanaanClient,
            UploadClient uploadClient,
            PokinPemdaPDFGenerator pokinPemdaPDFGenerator,
            PokinOpdPDFGenerator pokinOpdPDFGenerator,
            LocalStorageService localStorageService,
            TreeBuilder treeBuilder
            ) {
        this.perencanaanClient = perencanaanClient;
        this.uploadClient = uploadClient;
        this.pokinPemdaPDFGenerator = pokinPemdaPDFGenerator;
        this.pokinOpdPDFGenerator = pokinOpdPDFGenerator;
        this.localStorageService = localStorageService;
        this.treeBuilder = treeBuilder;
    }

    public String cetakPokinPemda(Integer pokinId) {

        PokinPemdaCetakResponse response =
                perencanaanClient.getPokinPemdaCetak(pokinId);

        List<Node> pokinTree = treeBuilder.build(response.item());

        byte[] pdf =
                pokinPemdaPDFGenerator.generatePDF(pokinTree);

        localStorageService.save("latest-pokin.pdf", pdf);

        return uploadClient.uploadFile(UploadRequest.pokinUpload(
                new ByteArrayResource(pdf),
                "pokin-pemda-%d.pdf".formatted(pokinId),
                "pokin-pemda",
                "pokin/pemda/%d".formatted(pokinId)
        )).url();
    }

    public String cetakPokinOpd(String kodeOpd, Integer tahun) {
        PokinOpdCetakResponse response =
                perencanaanClient.getPokinOpdCetak(kodeOpd, tahun);

        Node pokinTree = treeBuilder.buildPokinOpd(response.item());
        byte[] pdf =
                pokinOpdPDFGenerator.generatePDF(pokinTree);

        localStorageService.save("latest-pokin-opd.pdf", pdf);

        return uploadClient.uploadFile(UploadRequest.pokinUpload(
                new ByteArrayResource(pdf),
                "pokin-opd-%s-%d.pdf".formatted(kodeOpd, tahun),
                "pokin-opd",
                "pokin/opd/%s/%d".formatted(kodeOpd, tahun)
        )).url();
    }
}
