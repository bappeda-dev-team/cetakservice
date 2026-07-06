package cc.kertaskerja.cetakservice.client.perencanaan.domain;

import cc.kertaskerja.cetakservice.pdf.pokin.Node;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public record PokinPemda(
        Integer id,

        @JsonProperty("parent_id")
        Integer parentId,

        @JsonProperty("level_pohon")
        Integer levelPohon,

        @JsonProperty("jenis_pohon")
        String jenisPohon,

        @JsonProperty("nama_pohon")
        String namaPohon
) {
}
