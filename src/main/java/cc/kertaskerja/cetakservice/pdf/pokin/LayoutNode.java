package cc.kertaskerja.cetakservice.pdf.pokin;

import java.util.ArrayList;
import java.util.List;

public class LayoutNode {

    private final Node node;

    private float x;
    private float y;

    private float subtreeWidth;

    private final List<LayoutNode> children = new ArrayList<>();

    public LayoutNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSubtreeWidth() {
        return subtreeWidth;
    }

    public void setSubtreeWidth(float subtreeWidth) {
        this.subtreeWidth = subtreeWidth;
    }

    public List<LayoutNode> getChildren() {
        return children;
    }

    public void addChild(LayoutNode child) {
        this.children.add(child);
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }
}
