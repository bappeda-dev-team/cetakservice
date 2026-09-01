package cc.kertaskerja.cetakservice.pdf.pokin;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LayoutEngineTest {

    @Test
    void stacks_operational_extensions_below_operational_level_six() {
        Node levelEight1 = node(5, 3, 8, JenisPohon.OPERATIONAL_N);
        Node levelEight2 = node(6, 4, 8, JenisPohon.OPERATIONAL_N);
        Node operationalN1 = new Node(3, 2, 7, JenisPohon.OPERATIONAL_N, "Operational N 1", null,
                new ArrayList<>(List.of(levelEight1)));
        Node operationalN2 = new Node(4, 2, 7, JenisPohon.OPERATIONAL_N, "Operational N 2", null,
                new ArrayList<>(List.of(levelEight2)));
        Node operational = new Node(2, 1, 6, JenisPohon.OPERATIONAL, "Operational", null,
                new ArrayList<>(List.of(operationalN1, operationalN2)));
        Node root = new Node(1, 0, 5, JenisPohon.TACTICAL, "Tactical", null,
                new ArrayList<>(List.of(operational)));

        LayoutNode layoutOperational = new LayoutEngine().layout(root, ViewMode.OPD).root().getChildren().getFirst();
        LayoutNode firstExtension = layoutOperational.getChildren().getFirst();
        LayoutNode secondExtension = layoutOperational.getChildren().get(1);
        LayoutNode firstLevelEight = firstExtension.getChildren().getFirst();
        LayoutNode secondLevelEight = secondExtension.getChildren().getFirst();

        assertTrue(layoutOperational.isStackChildrenVertically());
        assertTrue(firstExtension.isStackChildrenVertically());
        assertEquals(layoutOperational.getX(), firstExtension.getX());
        assertEquals(layoutOperational.getX(), secondExtension.getX());
        assertEquals(layoutOperational.getX(), firstLevelEight.getX());
        assertEquals(layoutOperational.getX(), secondLevelEight.getX());
        assertTrue(secondExtension.getY() > firstExtension.getY());
        assertTrue(firstLevelEight.getY() > firstExtension.getY());
        assertTrue(secondLevelEight.getY() > secondExtension.getY());
    }

    @Test
    void stacks_many_operational_pemda_below_their_tactical_parent() {
        Node operational1 = node(2, 1, 6, JenisPohon.OPERATIONAL_PEMDA);
        Node operational2 = node(3, 1, 6, JenisPohon.OPERATIONAL_PEMDA);
        Node tactical = new Node(1, 0, 5, JenisPohon.TACTICAL_PEMDA, "Tactical", null,
                new ArrayList<>(List.of(operational1, operational2)));

        LayoutNode layoutTactical = new LayoutEngine().layout(tactical, ViewMode.PEMDA).root();
        LayoutNode firstOperational = layoutTactical.getChildren().getFirst();
        LayoutNode secondOperational = layoutTactical.getChildren().get(1);

        assertTrue(layoutTactical.isStackChildrenVertically());
        assertEquals(firstOperational.getX(), secondOperational.getX());
        assertTrue(secondOperational.getY() > firstOperational.getY());
    }

    @Test
    void keeps_operational_siblings_side_by_side_for_opd() {
        Node operational1 = node(2, 1, 6, JenisPohon.OPERATIONAL);
        Node operational2 = node(3, 1, 6, JenisPohon.OPERATIONAL);
        Node tactical = new Node(1, 0, 5, JenisPohon.TACTICAL, "Tactical", null,
                new ArrayList<>(List.of(operational1, operational2)));

        LayoutNode layoutTactical = new LayoutEngine().layout(tactical, ViewMode.OPD).root();
        LayoutNode firstOperational = layoutTactical.getChildren().getFirst();
        LayoutNode secondOperational = layoutTactical.getChildren().get(1);

        assertTrue(!layoutTactical.isStackChildrenVertically());
        assertTrue(secondOperational.getX() > firstOperational.getX());
        assertEquals(firstOperational.getY(), secondOperational.getY());
    }

    private static Node node(int id, int parentId, int level, JenisPohon jenisPohon) {
        return new Node(id, parentId, level, jenisPohon, jenisPohon.name(), null, new ArrayList<>());
    }
}
