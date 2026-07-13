package cc.kertaskerja.cetakservice.client.upload;

import org.springframework.web.client.RestClient;

public class UploadClient {
    private final RestClient restClient;

    public UploadClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public UploadSuccessResponse uploadFile() {
        UploadSuccessResponse
    }
}
