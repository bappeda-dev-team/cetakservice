package cc.kertaskerja.cetakservice.pdf.pokin;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ViewGenerator {
    // daftar halaman setiap pokin
    // by strategic
    // hal pertama - Tema, Sub Tema (all)
    // hal kedua - Tema, Sub Tema(1), Strategic
    // hal ketiga - Tema, Sub Tema(1), Strategic(1), Tact, Operasional
    // hal keempat - Tema, Sub Tema(1), Strategic(2), Tact, Operasional
    // ...

    public List<PagePlan> generate(Node root) {
        List<PagePlan> pagePlans = new ArrayList<>();

        generatePagePlan(root, new ArrayList<>(), pagePlans);

        return pagePlans;
    }

    private boolean shouldCreatePage(Node node) {
        return switch (node.jenisPohon()) {
            case "Strategic Pemda" -> true;
            default -> false;
        };
    }

    private void generatePagePlan(Node current,
                                  List<Node> ancestors,
                                  List<PagePlan> pagePlans) {
        if (shouldCreatePage(current)) {
            pagePlans.add(new PagePlan(List.copyOf(ancestors), current));
        }

        List<Node> nextAncestors = new ArrayList<>(ancestors);
        nextAncestors.add(current);

        for (Node child : current.children()) {
            generatePagePlan(child, nextAncestors, pagePlans);
        }
    }
}
