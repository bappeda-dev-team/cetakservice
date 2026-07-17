package cc.kertaskerja.cetakservice.client.perencanaan.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CrossCuttingPokin(
        @JsonProperty("is_crosscutting_diterima")
        boolean isCrosscuttingDiterima,

        @JsonProperty("nama_pohon_penerima")
        String namaPohonPenerima,

        @JsonProperty("nama_opd_penerima")
        String namaOpdPenerima,

        @JsonProperty("keterangan_crosscutting")
        String keteranganCrosscutting,

        @JsonProperty("status_crosscutting")
        String statusCrosscutting
) {
}
