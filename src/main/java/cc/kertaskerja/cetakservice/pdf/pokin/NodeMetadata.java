package cc.kertaskerja.cetakservice.pdf.pokin;

import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinOpd;

import java.util.List;

public record NodeMetadata(
        String kodeOpd,
        List<TujuanOpd> tujuanOpds
) {
    public static NodeMetadata fromOpd(PokinOpd item) {
        List<TujuanOpd> tujuanOpds = item.tujuanOpds().stream().map(tj ->
            new TujuanOpd(tj.tujuan())
        ).toList();

        return new NodeMetadata(item.kodeOpd(), tujuanOpds);
    }
}

record TujuanOpd(
        String tujuan
) {
}