package cc.kertaskerja.cetakservice.pokin.domain;

import cc.kertaskerja.cetakservice.client.perencanaan.PerencanaanClient;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinOpdCetakResponse;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinPemdaCetakResponse;
import cc.kertaskerja.cetakservice.client.upload.UploadClient;
import cc.kertaskerja.cetakservice.client.upload.domain.UploadRequest;
import cc.kertaskerja.cetakservice.client.upload.domain.UploadSuccessResponse;
import cc.kertaskerja.cetakservice.pdf.PokinOpdPDFGenerator;
import cc.kertaskerja.cetakservice.pdf.PokinPemdaPDFGenerator;
import cc.kertaskerja.cetakservice.pdf.pokin.Node;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;
import java.util.Optional;

@Service
public class PokinService {

    private final PerencanaanClient perencanaanClient;
    private final UploadClient uploadClient;
    private final PokinPemdaPDFGenerator pokinPemdaPDFGenerator;
    private final PokinOpdPDFGenerator pokinOpdPDFGenerator;

    private final TreeBuilder treeBuilder;

    public PokinService(
            PerencanaanClient perencanaanClient,
            UploadClient uploadClient,
            PokinPemdaPDFGenerator pokinPemdaPDFGenerator,
            PokinOpdPDFGenerator pokinOpdPDFGenerator,
            TreeBuilder treeBuilder
            ) {
        this.perencanaanClient = perencanaanClient;
        this.uploadClient = uploadClient;
        this.pokinPemdaPDFGenerator = pokinPemdaPDFGenerator;
        this.pokinOpdPDFGenerator = pokinOpdPDFGenerator;
        this.treeBuilder = treeBuilder;
    }

    public String cetakPokinPemda(Integer pokinId) {

        PokinPemdaCetakResponse response =
                perencanaanClient.getPokinPemdaCetak(pokinId);

        String version = response.version();
        String key = "pokin/pemda/%d/%s".formatted(pokinId, version);
        // cek ke upload service apakah key tersebut sudah ada
        Optional<UploadSuccessResponse> existing = uploadClient.findFile(key);

        if (existing.isPresent()) {
            return existing.get().url();
        }

        List<Node> pokinTree = treeBuilder.build(response.item());

        byte[] pdf =
                pokinPemdaPDFGenerator.generatePDF(pokinTree);

        // TODO: move to storage service
        //localStorageService.save("latest-pokin.pdf", pdf);

        return uploadClient.uploadFile(UploadRequest.pokinUpload(
                new ByteArrayResource(pdf),
                "pokin-pemda-%d-%s.pdf".formatted(pokinId, version),
                "pokin-pemda",
                key
        )).url();
    }

    public String cetakPokinOpd(String kodeOpd, Integer tahun) {
        PokinOpdCetakResponse response =
                perencanaanClient.getPokinOpdCetak(kodeOpd, tahun);

        String version = response.version();
        String key = "pokin/opd/%s/%d/%s".formatted(kodeOpd, tahun, version);
        // cek ke upload service apakah key tersebut sudah ada
        Optional<UploadSuccessResponse> existing = uploadClient.findFile(key);

        if (existing.isPresent()) {
            return existing.get().url();
        }

        Node pokinTree = treeBuilder.buildPokinOpd(response.item());
        byte[] pdf =
                pokinOpdPDFGenerator.generatePDF(pokinTree);

        // TODO: move to storage service
        //localStorageService.save("latest-pokin-opd.pdf", pdf);

        return uploadClient.uploadFile(UploadRequest.pokinUpload(
                new ByteArrayResource(pdf),
                "pokin-opd-%s-%d-%s.pdf".formatted(kodeOpd, tahun, version),
                "pokin-opd",
                key
        )).url();
    }
}
