package cc.kertaskerja.cetakservice.client.perencanaan.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PokinCetak(
        Integer id,

        @JsonProperty("parent_id")
        Integer parentId,

        @JsonProperty("level_pohon")
        Integer levelPohon,

        @JsonProperty("jenis_pohon")
        String jenisPohon,

        @JsonProperty("nama_pohon")
        String namaPohon,

        @JsonProperty("metadata_pohon")
        PokinMetadata pokinMetadata
) {
}
