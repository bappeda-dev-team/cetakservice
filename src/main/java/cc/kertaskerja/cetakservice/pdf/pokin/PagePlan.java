package cc.kertaskerja.cetakservice.pdf.pokin;

import java.util.List;

public record PagePlan(
        List<Node> ancestors,
        Node current // children diambil dari sini node.children()
) {
}
