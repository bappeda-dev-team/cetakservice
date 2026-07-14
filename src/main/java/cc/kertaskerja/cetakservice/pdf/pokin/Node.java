package cc.kertaskerja.cetakservice.pdf.pokin;

import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinCetak;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinOpd;

import java.util.ArrayList;
import java.util.List;

public record Node(
        Integer id,
        Integer parentId,
        Integer levelPohon,
        JenisPohon jenisPohon,
        String namaPohon,
        NodeMetadata nodeMetadata,
        List<Node> children
) {

    public Node {
        children = children == null ? new ArrayList<>() : children;
    }

    public static Node from(PokinCetak item) {
        JenisPohon jenisPohon = jenisPohonFinder(item.jenisPohon());

        return new Node(
                item.id(),
                item.parentId(),
                item.levelPohon(),
                jenisPohon,
                item.namaPohon(),
                NodeMetadata.empty(),
                new ArrayList<>()
        );
    }

    public static Node opdRoot(PokinOpd item) {

        return new Node(
                -1,
                -1,
                0,
                JenisPohon.OPD, // title pohon (strategic, tactical dst)
                item.namaOpd(),
                NodeMetadata.fromOpd(item),
                new ArrayList<>()
        );
    }

    private static JenisPohon jenisPohonFinder(String jenisPohon) {
        return switch (jenisPohon) {
            case "Tematik" -> JenisPohon.TEMATIK;
            case "Sub Tematik" -> JenisPohon.SUB_TEMATIK;
            case "Sub Sub Tematik" -> JenisPohon.SUB_SUB_TEMATIK;
            case "Strategic Pemda" -> JenisPohon.STRATEGIC_PEMDA;
            case "Tactical Pemda" -> JenisPohon.TACTICAL_PEMDA;
            case "Operational Pemda" -> JenisPohon.OPERATIONAL_PEMDA;
            case "Strategic" -> JenisPohon.STRATEGIC;
            case "Tactical" -> JenisPohon.TACTICAL;
            case "Operational" -> JenisPohon.OPERATIONAL;
            case "Operational N" -> JenisPohon.OPERATIONAL_N;
            default -> JenisPohon.POHON_KINERJA;
        };
    }
}
