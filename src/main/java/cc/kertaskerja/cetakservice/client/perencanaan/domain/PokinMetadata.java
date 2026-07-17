package cc.kertaskerja.cetakservice.client.perencanaan.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PokinMetadata(
        @JsonProperty("is_crosscutting")
        Boolean isCrosscutting,

        @JsonProperty("crosscutting_pokins")
        List<CrossCuttingPokin> crossCuttingPokins
) {
}
