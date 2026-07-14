package cc.kertaskerja.cetakservice.storage;

import cc.kertaskerja.cetakservice.client.upload.UploadClient;
import cc.kertaskerja.cetakservice.client.upload.domain.UploadRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Service
@Profile("!dev")
public class UploadPdfStorageService implements PdfStorageService {
    private final UploadClient uploadClient;

    public UploadPdfStorageService(UploadClient uploadClient) {
        this.uploadClient = uploadClient;
    }

    @Override
    public String storePdf(String fileName, byte[] pdf) {
        return uploadClient.uploadFile(UploadRequest.pokinUpload(
                new ByteArrayResource(pdf),
                fileName,
                fileName,
                "pokin-pemda"
        )).url();
    }
}
