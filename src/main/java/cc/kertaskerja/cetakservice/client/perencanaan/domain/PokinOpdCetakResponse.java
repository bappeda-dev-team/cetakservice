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