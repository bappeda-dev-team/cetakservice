package cc.kertaskerja.cetakservice.storage;

import cc.kertaskerja.cetakservice.client.upload.UploadClient;
import cc.kertaskerja.cetakservice.client.upload.domain.UploadRequest;
import cc.kertaskerja.cetakservice.client.upload.domain.UploadSuccessResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Profile("!dev")
public class UploadPdfStorageService implements PdfStorageService {
    private final UploadClient uploadClient;

    public UploadPdfStorageService(UploadClient uploadClient) {
        this.uploadClient = uploadClient;
    }

    @Override
    public Optional<String> findPdf(String key) {
        return uploadClient.findFile(key)
                .map(UploadSuccessResponse::url);
    }

    @Override
    public String storePdf(
            byte[] pdf,
            String fileName,
            String category,
            String key
    ) {

        UploadRequest request = UploadRequest.pokinUpload(
                new ByteArrayResource(pdf),
                fileName,
                category,
                key
        );
        return uploadClient.uploadFile(request).url();
    }
}
