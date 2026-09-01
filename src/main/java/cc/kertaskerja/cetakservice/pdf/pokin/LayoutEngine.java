package cc.kertaskerja.cetakservice.pdf.pokin;

import org.springframework.stereotype.Component;

import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.*;

@Component
public class LayoutEngine {

    public LayoutResult layout(Node root) {
        return layout(root, ViewMode.PEMDA);
    }

    public LayoutResult layout(Node root, ViewMode mode) {
        // konversi dari Node ke LayoutNode (skema node pohon)
        LayoutNode layoutRoot = toLayoutTree(root, mode, false);

        // cek lebar child dan assign ke layoutRoot (impure lah)
        calculateSubTreeSize(layoutRoot);

        layoutPosition(layoutRoot, 0, PAPER_MARGIN_TOP);

        LayoutBound layoutBound = calculateBounds(layoutRoot);

        // print(layoutRoot, 0);

        return new LayoutResult(layoutRoot, layoutBound);
    }

    private LayoutNode toLayoutTree(Node node, ViewMode mode, boolean belowOperationalLevelSix) {
        LayoutNode layout = new LayoutNode(node);
        boolean inOperationalLevelSixBranch = belowOperationalLevelSix || isOperationalLevelSix(node);

        for (Node child : node.children()) {
            layout.addChild(toLayoutTree(child, mode, inOperationalLevelSixBranch));
        }

        layout.setStackChildrenVertically(shouldStackChildrenVertically(node, mode, inOperationalLevelSixBranch));

        return layout;
    }

    private boolean shouldStackChildrenVertically(
            Node node, ViewMode mode, boolean inOperationalLevelSixBranch) {
        return switch (mode) {
            // Respons Pemda tidak lagi berisi Operational N. Semua sibling Operational
            // ditampilkan menurun agar lebar pohon tetap sesuai kertas.
            case PEMDA -> hasOnlyOperationalChildren(node);
            // Pada OPD, sibling Operational tetap menyamping. Hanya turunan
            // Operational N (termasuk crosscutting) yang ditampilkan menurun.
            case OPD -> inOperationalLevelSixBranch
                    && node.children().stream().anyMatch(this::isOperationalExtension);
        };
    }

    private boolean isOperationalLevelSix(Node node) {
        return node.levelPohon() != null
                && node.levelPohon() == 6
                && isOperational(node.jenisPohon());
    }

    private boolean isOperational(JenisPohon jenisPohon) {
        return jenisPohon == JenisPohon.OPERATIONAL
                || jenisPohon == JenisPohon.OPERATIONAL_PEMDA
                || jenisPohon == JenisPohon.OPERATIONAL_CROSSCUTTING
                || jenisPohon == JenisPohon.OPERATIONAL_N
                || jenisPohon == JenisPohon.OPERATIONAL_N_CROSSCUTTING;
    }

    private boolean isOperationalExtension(Node node) {
        return node.jenisPohon() == JenisPohon.OPERATIONAL_N
                || node.jenisPohon() == JenisPohon.OPERATIONAL_N_CROSSCUTTING;
    }

    private boolean hasOnlyOperationalChildren(Node node) {
        return !node.children().isEmpty()
                && node.children().stream().allMatch(child -> isOperational(child.jenisPohon()));
    }

    private void calculateSubTreeSize(LayoutNode node) {
        if (node.isLeaf()) {
            node.setSubtreeWidth(BOX_WIDTH);
            node.setSubtreeHeight(getNodeHeight(node.getNode()));
            return;
        }

        float totalWidth = 0f;
        float maxHeight = 0f;

        for (LayoutNode child : node.getChildren()) {
            calculateSubTreeSize(child);
            totalWidth += child.getSubtreeWidth();
            maxHeight = Math.max(maxHeight, child.getSubtreeHeight());
        }

        if (node.isStackChildrenVertically()) {
            float maxWidth = node.getChildren().stream()
                    .map(LayoutNode::getSubtreeWidth)
                    .max(Float::compare)
                    .orElse(BOX_WIDTH);
            float totalHeight = node.getChildren().stream()
                    .map(LayoutNode::getSubtreeHeight)
                    .reduce(0f, Float::sum);

            node.setSubtreeWidth(Math.max(maxWidth, BOX_WIDTH));
            node.setSubtreeHeight(getNodeHeight(node.getNode())
                    + LEVEL_GAP
                    + totalHeight
                    + (node.getChildren().size() - 1) * LEVEL_GAP);
            return;
        }

        totalWidth += (node.getChildren().size() - 1) * SIBLING_GAP;

        node.setSubtreeWidth(Math.max(totalWidth, BOX_WIDTH));
        node.setSubtreeHeight(getNodeHeight(node.getNode()) + LEVEL_GAP + maxHeight);
    }

    private float getNodeHeight(Node node) {
        return NodeSizeCalculator.getNodeSize(node).height();
    }

    private void layoutPosition(LayoutNode node, float areaLeft, float top) {

        node.setX(areaLeft + node.getSubtreeWidth() / 2f);
        node.setY(top);

        if (node.isStackChildrenVertically()) {
            float childTop = top + getNodeHeight(node.getNode()) + LEVEL_GAP;

            for (LayoutNode child : node.getChildren()) {
                layoutPosition(child, node.getX() - child.getSubtreeWidth() / 2f, childTop);
                childTop += child.getSubtreeHeight() + LEVEL_GAP;
            }
            return;
        }

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
