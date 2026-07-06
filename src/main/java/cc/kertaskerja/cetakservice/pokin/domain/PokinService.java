package cc.kertaskerja.cetakservice.pokin.domain;

import cc.kertaskerja.cetakservice.client.perencanaan.PerencanaanClient;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinPemda;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinPemdaCetakResponse;
import cc.kertaskerja.cetakservice.common.LocalStorageService;
import cc.kertaskerja.cetakservice.pdf.PageOrientation;
import cc.kertaskerja.cetakservice.pdf.PokinPemdaPDFGenerator;
import cc.kertaskerja.cetakservice.pdf.pokin.Node;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class PokinService {

    private final PerencanaanClient perencanaanClient;
    private final PokinPemdaPDFGenerator pokinPemdaPDFGenerator;
    private final LocalStorageService localStorageService;

    private final TreeBuilder treeBuilder;

    public PokinService(
            PerencanaanClient perencanaanClient,
            PokinPemdaPDFGenerator pokinPemdaPDFGenerator,
            LocalStorageService localStorageService,
            TreeBuilder treeBuilder
            ) {
        this.perencanaanClient = perencanaanClient;
        this.pokinPemdaPDFGenerator = pokinPemdaPDFGenerator;
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

        return "/dev/latest-pokin";
    }
}
