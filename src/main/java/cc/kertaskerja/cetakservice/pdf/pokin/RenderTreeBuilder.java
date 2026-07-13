package cc.kertaskerja.cetakservice.pdf.pokin;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RenderTreeBuilder {

    public Node build(PagePlan pagePlan) {

        if (pagePlan.ancestors().isEmpty()) {
            return cloneSubTree(pagePlan.current());
        }

        Node root =
                cloneNode(pagePlan.ancestors().getFirst());

        Node cursor = root;

        for (int i = 1; i < pagePlan.ancestors().size(); i++) {

            Node copy =
                    cloneNode(pagePlan.ancestors().get(i));

            cursor.children().add(copy);

            cursor = copy;
        }

        cursor.children().add(
                cloneSubTree(pagePlan.current())
        );

        return root;
    }

    private Node cloneNode(Node node) {
        return new Node(
                node.id(),
                node.parentId(),
                node.levelPohon(),
                node.jenisPohon(),
                node.namaPohon(),
                null,
                new ArrayList<>()
        );
    }

    private Node cloneSubTree(Node node) {

        Node copy = cloneNode(node);

        for (Node child : node.children()) {
            copy.children().add(
                    cloneSubTree(child)
            );
        }

        return copy;
    }
}
