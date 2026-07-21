package cc.kertaskerja.cetakservice.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@ConfigurationProperties(prefix = "kertaskerja")
public record KertaskerjaProperties(
        @NotBlank String upMessage,
        @NotEmpty List<String> allowedHosts,
        Map<String, ServiceProperties> services) {

    public record ServiceProperties(
            String name,
            String url,
            String version) {
    }
}
