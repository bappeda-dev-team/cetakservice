package cc.kertaskerja.cetakservice.pdf.pokin;

import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinCetak;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinOpd;

import java.util.List;

public record NodeMetadata(
        String nomor,
        String kodeOpd,
        List<TujuanOpd> tujuanOpds,
        boolean isCrosscutting,
        List<CrossCuttingPokin> crosscuttingPokins,
        List<IndikatorPokin> indikatorPokins
) {
    public static NodeMetadata fromOpd(PokinOpd item) {
        List<TujuanOpd> tujuanOpds = item.tujuanOpds().stream().map(tj ->
                new TujuanOpd(tj.tujuan())
        ).toList();

        return new NodeMetadata("1",
                item.kodeOpd(),
                tujuanOpds,
                false,
                List.of(),
                List.of());
    }

    public static NodeMetadata fromPokin(PokinCetak item) {
        if (item.pokinMetadata() == null) {
            return empty();
        }

        List<CrossCuttingPokin> crosscutItems = item.pokinMetadata().crossCuttingPokins() == null
                ? List.of()
                : item.pokinMetadata().crossCuttingPokins()
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

        List<IndikatorPokin> indikatorItems = item.pokinMetadata().indikatorPokins() == null
                ? List.of()
                : item.pokinMetadata().indikatorPokins().stream()
                .map(indikator -> new IndikatorPokin(
                        indikator.namaIndikator(),
                        indikator.targets() == null ? List.of() : indikator.targets().stream()
                                .map(target -> new TargetIndikatorPokin(
                                        target.target(), target.satuan(), target.tahun()))
                                .toList()))
                .toList();

        return new NodeMetadata(
                null,
                null,
                List.of(),
                Boolean.TRUE.equals(item.pokinMetadata().isCrosscutting()),
                crosscutItems,
                indikatorItems
        );
    }

    public static NodeMetadata empty() {
        return new NodeMetadata(
                null, null,
                List.of(),
                false,
                List.of(),
                List.of()
        );
    }

    public NodeMetadata withNomor(String nomor) {
        return new NodeMetadata(
                nomor,
                kodeOpd,
                tujuanOpds,
                isCrosscutting,
                crosscuttingPokins,
                indikatorPokins
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

record IndikatorPokin(
        String namaIndikator,
        List<TargetIndikatorPokin> targets
) {
}

record TargetIndikatorPokin(
        String target,
        String satuan,
        String tahun
) {
}
