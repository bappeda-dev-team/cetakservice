package cc.kertaskerja.cetakservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "kertaskerja")
public record KertaskerjaProperties(
        String upMessage,
        Map<String, ServiceProperties> services
) {

    public record ServiceProperties(
            String name,
            String url,
            String version
    ) {}
}
