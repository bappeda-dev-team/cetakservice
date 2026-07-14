package cc.kertaskerja.cetakservice.client.upload.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UploadSuccessResponse(
        long id,
        @JsonProperty("object_key")
        String objectKey,
        String bucket,
        @JsonProperty("original_name")
        String originalName,
        String extension,
        @JsonProperty("content_type")
        String contentType,
        @JsonProperty("file_size")
        long fileSize,
        @JsonProperty("checksum_algorithm")
        String checksumAlgorithm,
        String checksum,
        String category,
        @JsonProperty("owner_type")
        String ownerType,
        @JsonProperty("owner_id")
        String ownerId,
        String visibility,
        @JsonProperty("created_by")
        String createdBy,
        String url
) {
}
