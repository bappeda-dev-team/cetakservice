package cc.kertaskerja.cetakservice.pdf.pokin;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LayoutPaginatorTest {

    @Test
    void creates_continuation_pages_for_a_tall_operational_branch() {
        List<Node> operationals = new ArrayList<>();
        for (int index = 0; index < 50; index++) {
            operationals.add(new Node(index + 2, 1, 6, JenisPohon.OPERATIONAL_PEMDA,
                    "Operational " + index, null, new ArrayList<>()));
        }
        Node tactical = new Node(1, 0, 5, JenisPohon.TACTICAL_PEMDA, "Tactical", null, operationals);
        LayoutResult layout = new LayoutEngine().layout(tactical);

        List<PageSegment> segments = new LayoutPaginator().paginate(layout, new PDRectangle(1000, 600));

        assertTrue(segments.size() > 1);
        assertEquals(1, segments.getFirst().number());
        assertEquals(segments.size(), segments.getLast().total());
        assertTrue(segments.get(1).startY() > segments.getFirst().startY());
    }
}
