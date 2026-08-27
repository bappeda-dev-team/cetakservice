package cc.kertaskerja.cetakservice.pdf.pokin;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RenderTreeBuilderTest {

    @Test
    void assigns_parent_number_to_level_eight_and_deeper_nodes() {
        Node levelNine = node(5, 4, 9, List.of());
        Node levelEight1 = node(3, 2, 8, List.of());
        Node levelEight2 = node(4, 2, 8, List.of(levelNine));
        Node levelSeven = node(2, 1, 7, List.of(levelEight1, levelEight2));
        Node operational = node(1, 0, 6, List.of(levelSeven));

        Node rendered = new RenderTreeBuilder()
                .build(new PagePlan(1, List.of(), operational))
                .root();

        Node renderedLevelSeven = rendered.children().getFirst();
        Node renderedLevelEight1 = renderedLevelSeven.children().getFirst();
        Node renderedLevelEight2 = renderedLevelSeven.children().get(1);
        Node renderedLevelNine = renderedLevelEight2.children().getFirst();

        assertEquals("1", renderedLevelSeven.nodeMetadata().nomor());
        assertEquals("1.1", renderedLevelEight1.nodeMetadata().nomor());
        assertEquals("1.2", renderedLevelEight2.nodeMetadata().nomor());
        assertEquals("1.2.1", renderedLevelNine.nodeMetadata().nomor());
    }

    private static Node node(int id, int parentId, int level, List<Node> children) {
        return new Node(id, parentId, level, JenisPohon.OPERATIONAL_N, "Node", NodeMetadata.empty(),
                new ArrayList<>(children));
    }
}
