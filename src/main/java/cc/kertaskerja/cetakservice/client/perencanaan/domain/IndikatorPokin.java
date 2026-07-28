package cc.kertaskerja.cetakservice.client.perencanaan.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record IndikatorPokin(
        @JsonProperty("nama_indikator")
        String namaIndikator,

        List<TargetIndikatorPokin> targets
) {
}
