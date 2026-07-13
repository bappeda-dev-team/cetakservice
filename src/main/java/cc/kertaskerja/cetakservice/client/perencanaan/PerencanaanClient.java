package cc.kertaskerja.cetakservice.client.perencanaan;

import cc.kertaskerja.cetakservice.client.perencanaan.domain.PerencanaanWebResponse;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinOpdCetakResponse;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinPemdaCetakResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PerencanaanClient {
    private final RestClient restClient;

    public PerencanaanClient(
            @Qualifier("perencanaanRestClient")
            RestClient restClient
    ) {
        this.restClient = restClient;
    }

    public PokinPemdaCetakResponse getPokinPemdaCetak(Integer pokinId) {
        PerencanaanWebResponse<PokinPemdaCetakResponse> response = restClient
                .get()
                .uri("/pohon_kinerja/cetak/{pokinId}", pokinId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (response == null || response.data() == null) {
            throw new IllegalStateException("Perencanaan service returned empty response");
        }

        return response.data();
    }

    public PokinOpdCetakResponse getPokinOpdCetak(String kodeOpd, Integer tahun) {
        PerencanaanWebResponse<PokinOpdCetakResponse> response = restClient
                .get()
                .uri("/pohon_kinerja_opd/cetak/{kodeOpd}/{tahun}", kodeOpd, tahun)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (response == null || response.data() == null) {
            throw new IllegalStateException("Perencanaan service returned empty response");
        }

        return response.data();
    }
}
