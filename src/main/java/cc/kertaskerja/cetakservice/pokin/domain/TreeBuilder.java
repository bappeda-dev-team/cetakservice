package cc.kertaskerja.cetakservice.pokin.domain;

import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinCetak;
import cc.kertaskerja.cetakservice.client.perencanaan.domain.PokinOpd;
import cc.kertaskerja.cetakservice.pdf.pokin.Node;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TreeBuilder {

    public List<Node> build(List<PokinCetak> items) {
        Map<Integer, Node> nodeMap = items.stream()
                .map(Node::from)
                .collect(Collectors.toMap(Node::id, Function.identity()));

        List<Node> roots = new ArrayList<>();

        for (Node  node : nodeMap.values()) {
            if (node.parentId() == 0) {
                roots.add(node);
                continue;
            }

            Node parent = nodeMap.get(node.parentId());

            if (parent == null) {
                throw new IllegalStateException("Parent " + node.parentId() + " not found");
            }

            parent.children().add(node);
        }

        return roots;
    }

    public Node buildPokinOpd(PokinOpd item) {
        Node root = Node.opdRoot(item);

        Map<Integer, Node> nodeMap = item.pohonKinerjas().stream().map(Node::from)
                .collect(Collectors.toMap(Node::id, Function.identity()));

        for (Node  node : nodeMap.values()) {
            if (node.parentId() == 0) {
                root.children().add(node);
                continue;
            }

            Node parent = nodeMap.get(node.parentId());

            if (parent == null) {
                throw new IllegalStateException("Parent " + node.parentId() + " not found");
            }

            parent.children().add(node);
        }

        return root;
    }

}
