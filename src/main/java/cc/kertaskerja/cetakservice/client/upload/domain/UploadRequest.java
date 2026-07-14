package cc.kertaskerja.cetakservice.client.upload.domain;

import org.springframework.core.io.Resource;

public record UploadRequest(
        Resource file,
        String fileName,
        String category,
        String ownerType,
        String ownerId,
        String createdBy,
        String visibility,
        String key
) {
    public static UploadRequest pokinUpload(
            String fileName,
            String category,
            String key
    ) {
        return new UploadRequest(
                null,
                fileName,
                category,
                "service",
                "cetak-service",
                "cetak-service",
                "public",
                key
        );
    }
}
