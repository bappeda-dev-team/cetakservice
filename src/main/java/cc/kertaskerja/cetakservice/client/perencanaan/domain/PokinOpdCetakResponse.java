package cc.kertaskerja.cetakservice.client.perencanaan.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public record PokinOpdCetakResponse(
        String nama,
        String version,
        OffsetDateTime time,
        PokinOpd item
) {
}
record PokinOpd(
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

record TujuanOpd(
        Integer id,
        @JsonProperty("kode_opd")
        String kodeOpd,
        String tujuan,
        List<IndikatorTujuan> indikator
) {

}

record IndikatorTujuan(
        String indikator,
        List<TargetTujuan> targets
) {
}

record TargetTujuan(
        String tahun,
        String target,
        String satuan
) {}