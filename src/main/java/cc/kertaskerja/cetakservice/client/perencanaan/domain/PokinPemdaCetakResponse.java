package cc.kertaskerja.cetakservice.client.perencanaan.domain;

import java.time.OffsetDateTime;
import java.util.List;

public record PokinPemdaCetakResponse(
        String nama,
        String version,
        OffsetDateTime time,
        List<PokinCetak> item
) {
}
