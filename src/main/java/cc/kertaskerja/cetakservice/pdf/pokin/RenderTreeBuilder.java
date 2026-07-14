package cc.kertaskerja.cetakservice.pdf.pokin;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RenderTreeBuilder {

    public RenderTree build(PagePlan pagePlan) {

        Node current;

        if (pagePlan.ancestors().isEmpty()) {
            current = cloneSubTree(pagePlan.current());

            return new RenderTree(current, current);
        }

            Node root = cloneNode(pagePlan.ancestors().getFirst());

            Node cursor = root;

            for (int i = 1; i < pagePlan.ancestors().size(); i++) {

                Node copy = cloneNode(pagePlan.ancestors().get(i));

                cursor.children().add(copy);

                cursor = copy;
            }

            current = cloneSubTree(pagePlan.current(), pagePlan.sequence());

            cursor.children().add(current);

        return new RenderTree(root, current);
    }

    public Node buildCover(Node root) {

        Node copy = cloneNode(root);

        int nomor = 1;

        for (Node child : root.children()) {

            copy.children().add(
                    cloneNode(child, nomor++)
            );
        }

        return copy;
    }

    private Node cloneNode(Node node) {
        return cloneNode(node, null);
    }

    private Node cloneNode(Node node, Integer nomor) {

        NodeMetadata metadata = nomor == null
                ? node.nodeMetadata()
                : node.nodeMetadata().withNomor(nomor);

        return new Node(
                node.id(),
                node.parentId(),
                node.levelPohon(),
                node.jenisPohon(),
                node.namaPohon(),
                metadata,
                new ArrayList<>()
        );
    }

    private Node cloneSubTree(Node node) {
        return cloneSubTree(node, null);
    }

    private Node cloneSubTree(Node node, Integer nomor) {

        Node copy = cloneNode(node, nomor);

        int childNumber = 1;

        for (Node child : node.children()) {
            copy.children().add(
                    cloneSubTree(child, childNumber++)
            );
        }

        return copy;
    }
}
