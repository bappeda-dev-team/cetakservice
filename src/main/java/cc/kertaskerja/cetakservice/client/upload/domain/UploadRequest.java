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
            Resource file,
            String fileName,
            String category,
            String key
    ) {
        return new UploadRequest(
                file,
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
