package cc.kertaskerja.cetakservice.storage;

import java.util.Optional;

public interface PdfStorageService {
    Optional<String> findPdf(String key);
    String storePdf(
            byte[] pdf,
            String fileName,
            String category,
            String key);
}
