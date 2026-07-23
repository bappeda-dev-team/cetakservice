package cc.kertaskerja.cetakservice.pdf.pokin;

import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinCetak;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinOpd;

import java.util.List;

public record NodeMetadata(
        Integer nomor,
        String kodeOpd,
        List<TujuanOpd> tujuanOpds,
        boolean isCrosscutting,
        List<CrossCuttingPokin> crosscuttingPokins
) {
    public static NodeMetadata fromOpd(PokinOpd item) {
        List<TujuanOpd> tujuanOpds = item.tujuanOpds().stream().map(tj ->
                new TujuanOpd(tj.tujuan())
        ).toList();

        return new NodeMetadata(1,
                item.kodeOpd(),
                tujuanOpds,
                false,
                null);
    }

    public static NodeMetadata fromPokin(PokinCetak item) {
        if (item.pokinMetadata() == null) {
            return empty();
        }

        if (!item.pokinMetadata().isCrosscutting()) {
            return empty();
        }

        List<CrossCuttingPokin> crosscutItems = item.pokinMetadata().crossCuttingPokins()
                .stream().map(cp ->
                        new CrossCuttingPokin(
                                cp.namaPohonPemberi(),
                                cp.namaOpdPemberi(),
                                cp.namaPohonPenerima(),
                                cp.namaOpdPenerima(),
                                cp.keteranganCrosscutting(),
                                cp.statusCrosscutting()
                        ))
                .toList();

        return new NodeMetadata(
                null,
                null,
                List.of(),
                true,
                crosscutItems
        );
    }

    public static NodeMetadata empty() {
        return new NodeMetadata(
                null, null,
                List.of(),
                false,
                List.of()
        );
    }

    public NodeMetadata withNomor(Integer nomor) {
        return new NodeMetadata(
                nomor,
                kodeOpd,
                tujuanOpds,
                isCrosscutting,
                crosscuttingPokins
        );
    }
}

record CrossCuttingPokin(
        String namaPohonPemberi,
        String namaOpdPemberi,
        String namaPohonPenerima,
        String namaOpdPenerima,
        String keteranganCrosscutting,
        String statusCrosscutting
) {
}

record TujuanOpd(
        String namaTujuan
) {
}
