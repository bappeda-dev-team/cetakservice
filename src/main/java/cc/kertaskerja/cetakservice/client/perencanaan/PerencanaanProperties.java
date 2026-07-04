package cc.kertaskerja.cetakservice.client.perencanaan;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("kertaskerja.services.perencanaan")
public record PerencanaanProperties(
        String name,
        String url
) {
}
