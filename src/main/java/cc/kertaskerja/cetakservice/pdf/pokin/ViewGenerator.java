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

        generatePagePlan(root, new ArrayList<>(), pagePlans, 1);

        return pagePlans;
    }

    // split page by jenis pohon
    private boolean shouldCreatePage(Node node) {
        return switch (node.jenisPohon()) {
            case JenisPohon.SUB_TEMATIK -> true;
            case JenisPohon.STRATEGIC_PEMDA -> node.isOpd();
            case JenisPohon.STRATEGIC -> node.isOpd();
            default -> false;
        };
    }

    private void generatePagePlan(Node current,
                                  List<Node> ancestors,
                                  List<PagePlan> pagePlans,
                                  Integer sequence) {
        if (shouldCreatePage(current)) {
            pagePlans.add(new PagePlan(sequence, List.copyOf(ancestors), current));
        }

        List<Node> nextAncestors = new ArrayList<>(ancestors);
        nextAncestors.add(current);

        int childSequence = 1;

        for (Node child : current.children()) {

            Integer nextSequence = sequence;

            if (shouldCreatePage(child)) {
                nextSequence = childSequence++;
            }

            generatePagePlan(
                    child,
                    nextAncestors,
                    pagePlans,
                    nextSequence
            );
        }
    }
}
