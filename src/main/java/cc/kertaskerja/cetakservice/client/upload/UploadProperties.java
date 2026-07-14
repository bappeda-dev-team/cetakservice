package cc.kertaskerja.cetakservice.client.upload;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kertaskerja.services.upload")
public record UploadProperties(
        String name,
        String url
) {
}
