package cc.kertaskerja.cetakservice.client.perencanaan.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TujuanOpd(
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
