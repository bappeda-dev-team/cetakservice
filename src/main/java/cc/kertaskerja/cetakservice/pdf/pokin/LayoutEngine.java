package cc.kertaskerja.cetakservice.pdf.pokin;

import org.springframework.stereotype.Component;

import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.*;

@Component
public class LayoutEngine {

    public LayoutResult layout(Node root) {
        // konversi dari Node ke LayoutNode (skema node pohon)
        LayoutNode layoutRoot = toLayoutTree(root);

        // cek lebar child dan assign ke layoutRoot (impure lah)
        calculateSubTreeWidth(layoutRoot);

        layoutPosition(layoutRoot, 0, PAPER_MARGIN_TOP);

        LayoutBound layoutBound = calculateBounds(layoutRoot);

        // print(layoutRoot, 0);

        return new LayoutResult(layoutRoot, layoutBound);
    }

    private LayoutNode toLayoutTree(Node node) {
        LayoutNode layout = new LayoutNode(node);

        for (Node child : node.children()) {
            layout.addChild(toLayoutTree(child));
        }

        return layout;
    }

    private float calculateSubTreeWidth(LayoutNode node) {
        if (node.isLeaf()) {
            node.setSubtreeWidth(BOX_WIDTH);
            return BOX_WIDTH;
        }

        float total = 0f;

        for (LayoutNode child : node.getChildren()) {
            total += calculateSubTreeWidth(child);
        }

        total += (node.getChildren().size() - 1) * SIBLING_GAP;

        float width = Math.max(total, BOX_WIDTH);

        node.setSubtreeWidth(width);

        return width;
    }

    private float getNodeHeight(Node node) {
        if (node.nodeMetadata() != null && node.nodeMetadata().isCrosscutting()) {
            return 150f; // samakan dengan CROSSCUTTING_BOX_HEIGHT saat ini
        }

        if (node.nodeMetadata() != null
                && node.nodeMetadata().tujuanOpds() != null
                && !node.nodeMetadata().tujuanOpds().isEmpty()) {
            return 80f; // samakan dengan TUJUAN_OPD_BOX_HEIGHT
        }

        return BOX_HEIGHT;
    }

    private void layoutPosition(LayoutNode node, float areaLeft, float top) {

        node.setX(areaLeft + node.getSubtreeWidth() / 2f);
        node.setY(top);

        float childAreaLeft = areaLeft;

        for (LayoutNode child : node.getChildren()) {

            float childWidth = child.getSubtreeWidth();

            layoutPosition(
                    child,
                    childAreaLeft,
                    top + getNodeHeight(node.getNode()) + LEVEL_GAP);

            childAreaLeft += childWidth + SIBLING_GAP;
        }
    }

    private static class BoundAccumulator {
        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;

        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;
    }

    private LayoutBound calculateBounds(LayoutNode root) {
        BoundAccumulator acc = new BoundAccumulator();
        visit(root, acc);
        return new LayoutBound(acc.minX, acc.maxX, acc.minY, acc.maxY);
    }

    private void visit(LayoutNode node, BoundAccumulator acc) {
        acc.minX = Math.min(acc.minX, node.getX());
        acc.maxX = Math.max(acc.maxX, node.getX());

        acc.minY = Math.min(acc.minY, node.getY());
        acc.maxY = Math.max(acc.maxY, node.getY());

        for (LayoutNode child : node.getChildren()) {
            visit(child, acc);
        }
    }
}
