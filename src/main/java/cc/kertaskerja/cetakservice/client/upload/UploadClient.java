package cc.kertaskerja.cetakservice.client.upload;

import cc.kertaskerja.cetakservice.client.upload.domain.UploadRequest;
import cc.kertaskerja.cetakservice.client.upload.domain.UploadSuccessResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Component
public class UploadClient {
    private final RestClient restClient;

    public UploadClient(
            @Qualifier("uploadRestClient")
            RestClient restClient) {
        this.restClient = restClient;
    }

    public UploadSuccessResponse uploadFile(UploadRequest request) {
        MultipartBodyBuilder body = new MultipartBodyBuilder();
        body.part("file", request.file())
                .filename(request.fileName())
                .contentType(MediaType.APPLICATION_PDF);
        addPart(body, "category", request.category());
        addPart(body, "owner_type", request.ownerType());
        addPart(body, "owner_id", request.ownerId());
        addPart(body, "created_by", request.createdBy());
        addPart(body, "visibility", request.visibility());
        addPart(body, "key", request.key());

        UploadSuccessResponse response = restClient.post()
                .uri("/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body.build())
                .retrieve()
                .body(UploadSuccessResponse.class);

        if (response == null) {
            throw new IllegalStateException("upload-service returned an empty response");
        }

        return response;
    }

    private static void addPart(MultipartBodyBuilder body, String name, String value) {
        if (StringUtils.hasText(value)) {
            body.part(name, value);
        }
    }
}
