package cc.kertaskerja.cetakservice.client.perencanaan.domain;

import java.time.OffsetDateTime;

public record PokinPemdaCetakResponse(
        String nama,
        String version,
        OffsetDateTime time,
        PokinPemda item
) {
}
