package cc.kertaskerja.cetakservice.pokin.web;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CetakPokinOpdRequest(
        @JsonProperty("kode_opd")
        String kodeOpd,
        Integer tahun
) {
}
