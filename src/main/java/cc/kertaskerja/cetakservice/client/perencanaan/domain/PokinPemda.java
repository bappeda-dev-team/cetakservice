package cc.kertaskerja.cetakservice.client.perencanaan.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PokinPemda(
        Integer id,

        @JsonProperty("level_pohon")
        Integer levelPohon,

        @JsonProperty("jenis_pohon")
        String jenisPohon,

        @JsonProperty("nama_pohon")
        String namaPohon,

        List<PokinPemda> childs
) {
}
