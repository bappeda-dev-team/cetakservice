package cc.kertaskerja.cetakservice.pdf.pokin;

import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinCetak;

import java.util.List;
import java.util.ArrayList;

public record Node(
        Integer id,
        Integer parentId,
        Integer levelPohon,
        String jenisPohon,
        String namaPohon,
        List<Node> children
) {

    public Node {
        children = children == null ? new ArrayList<>() : children;
    }

    public static Node from(PokinCetak item) {
        return new Node(
                item.id(),
                item.parentId(),
                item.levelPohon(),
                item.jenisPohon(),
                item.namaPohon(),
                null
        );
    }

}
