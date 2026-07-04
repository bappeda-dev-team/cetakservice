package cc.kertaskerja.cetakservice.pokin.domain;

import cc.kertaskerja.cetakservice.client.perencanaan.PerencanaanClient;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinPemdaCetakResponse;
import cc.kertaskerja.cetakservice.common.LocalStorageService;
import cc.kertaskerja.cetakservice.pdf.PageOrientation;
import cc.kertaskerja.cetakservice.pdf.PokinPemdaPDFGenerator;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class PokinService {

    private final PerencanaanClient perencanaanClient;
    private final PokinPemdaPDFGenerator pokinPemdaPDFGenerator;
    private final LocalStorageService localStorageService;

    public PokinService(
            PerencanaanClient perencanaanClient,
            PokinPemdaPDFGenerator pokinPemdaPDFGenerator,
            LocalStorageService localStorageService
            ) {
        this.perencanaanClient = perencanaanClient;
        this.pokinPemdaPDFGenerator = pokinPemdaPDFGenerator;
        this.localStorageService = localStorageService;
    }

    public String cetakPokinPemda(Integer pokinId) {

        PokinPemdaCetakResponse response =
                perencanaanClient.getPokinPemdaCetak(pokinId);


        byte[] pdf =
                pokinPemdaPDFGenerator.generatePDF(response, PageOrientation.LANDSCAPE);

        localStorageService.save("latest-pokin.pdf", pdf);

        return "/dev/latest-pokin";
    }
}
