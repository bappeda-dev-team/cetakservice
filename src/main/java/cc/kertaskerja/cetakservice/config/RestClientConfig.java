package cc.kertaskerja.cetakservice.config;

import cc.kertaskerja.cetakservice.client.perencanaan.PerencanaanProperties;
import cc.kertaskerja.cetakservice.client.upload.UploadProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient perencanaanRestClient(
            RestClient.Builder builder,
            PerencanaanProperties perencanaanProperties
    ) {
        return builder
                .baseUrl(perencanaanProperties.url())
                .build();
    }

    @Bean
    public RestClient uploadRestClient(
            RestClient.Builder builder,
            UploadProperties uploadProperties
    ) {
        return builder
                .baseUrl(uploadProperties.url())
                .build();
    }
}
