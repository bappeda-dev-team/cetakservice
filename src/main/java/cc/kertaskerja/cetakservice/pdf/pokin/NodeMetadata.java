package cc.kertaskerja.cetakservice.pdf.pokin;

import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinOpd;

import java.util.List;

public record NodeMetadata(
        Integer nomor,
        String kodeOpd,
        List<TujuanOpd> tujuanOpds
) {
    public static NodeMetadata fromOpd(PokinOpd item) {
        List<TujuanOpd> tujuanOpds = item.tujuanOpds().stream().map(tj ->
            new TujuanOpd(tj.tujuan())
        ).toList();

        return new NodeMetadata(1, item.kodeOpd(), tujuanOpds);
    }

    public static NodeMetadata of(Integer nomor) {
        return new NodeMetadata(nomor, null, null);
    }

    public static NodeMetadata empty() {
        return new NodeMetadata(null, null, null);
    }

    public NodeMetadata withNomor(Integer nomor) {
        return new NodeMetadata(
                nomor,
                kodeOpd,
                tujuanOpds
        );
    }
}

record TujuanOpd(
        String namaTujuan
) {
}