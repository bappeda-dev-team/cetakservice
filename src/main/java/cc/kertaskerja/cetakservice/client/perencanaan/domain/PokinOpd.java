package cc.kertaskerja.cetakservice.client.perencanaan.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PokinOpd(
        Integer tahun,
        @JsonProperty("kode_opd")
        String kodeOpd,
        @JsonProperty("nama_opd")
        String namaOpd,
        @JsonProperty("tujuan_opds")
        List<TujuanOpd> tujuanOpds,
        @JsonProperty("pohon_kinerjas")
        List<PokinCetak> pohonKinerjas
) {
}
