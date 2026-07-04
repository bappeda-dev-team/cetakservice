package cc.kertaskerja.cetakservice.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KertaskerjaInfoContributor implements InfoContributor {
    private final KertaskerjaProperties kertaskerjaProperties;

    public KertaskerjaInfoContributor(KertaskerjaProperties kertaskerjaProperties) {
        this.kertaskerjaProperties = kertaskerjaProperties;
    }

    public record ServiceInfo(
            String name,
            String url
    ) {}

    @Override
    public void contribute(Info.Builder builder) {
        var services = kertaskerjaProperties.services()
                        .entrySet().stream()
                        .map(entry -> new ServiceInfo(
                                entry.getKey(),
                                entry.getValue().url()
                        )).toList();

        builder.withDetail("kertaskerja", Map.of(
                "upMessage", kertaskerjaProperties.upMessage(),
                "services", services
        ));
    }
}
