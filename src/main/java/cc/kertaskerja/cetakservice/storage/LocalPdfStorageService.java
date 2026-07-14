package cc.kertaskerja.cetakservice.storage;

import cc.kertaskerja.cetakservice.common.LocalStorageService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Profile("dev")
public class LocalPdfStorageService implements PdfStorageService {

    private final LocalStorageService localStorageService;

    public LocalPdfStorageService(LocalStorageService localStorageService) {
        this.localStorageService = localStorageService;
    }

    @Override
    public Optional<String> findPdf(String key) {
        // better bikin baru terus untuk dev
        return Optional.empty();
    }

    @Override
    public String storePdf(
            byte[] pdf,
            String fileName,
            String category,
            String key
    ) {
        localStorageService.save(key, fileName, pdf);

        return "/dev/result/%s/%s".formatted(key, fileName);
    }
}
