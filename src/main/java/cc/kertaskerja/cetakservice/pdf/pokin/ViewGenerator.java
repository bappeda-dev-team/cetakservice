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

    public List<PagePlan> generate(Node root, ViewMode mode) {
        List<PagePlan> pagePlans = new ArrayList<>();

        generatePagePlan(mode, root, new ArrayList<>(), pagePlans, 1);

        return pagePlans;
    }

    // split page by jenis pohon
    private boolean shouldCreatePage(Node node, ViewMode mode) {
        return switch (mode) {
            case PEMDA -> node.jenisPohon() == JenisPohon.SUB_TEMATIK;
            case OPD -> node.jenisPohon() == JenisPohon.STRATEGIC_PEMDA || node.jenisPohon() == JenisPohon.STRATEGIC || node.jenisPohon() == JenisPohon.STRATEGIC_CROSSCUTTING;
        };
    }

    private void generatePagePlan(ViewMode mode,
                                  Node current,
                                  List<Node> ancestors,
                                  List<PagePlan> pagePlans,
                                  Integer sequence) {
        if (shouldCreatePage(current, mode)) {
            pagePlans.add(new PagePlan(sequence, List.copyOf(ancestors), current));
        }

        List<Node> nextAncestors = new ArrayList<>(ancestors);
        nextAncestors.add(current);

        int childSequence = 1;

        for (Node child : current.children()) {

            Integer nextSequence = sequence;

            if (shouldCreatePage(child, mode)) {
                nextSequence = childSequence++;
            }

            generatePagePlan(
                    mode,
                    child,
                    nextAncestors,
                    pagePlans,
                    nextSequence
            );
        }
    }
}
