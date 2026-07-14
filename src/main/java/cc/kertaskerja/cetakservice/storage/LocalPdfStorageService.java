package cc.kertaskerja.cetakservice.storage;

import cc.kertaskerja.cetakservice.common.LocalStorageService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class LocalPdfStorageService implements PdfStorageService {

    private final LocalStorageService localStorageService;

    public LocalPdfStorageService(LocalStorageService localStorageService) {
        this.localStorageService = localStorageService;
    }

    @Override
    public String storePdf(String fileName, byte[] pdf) {
        localStorageService.save("latest-pokin.pdf", pdf);
        return "";
    }
}
