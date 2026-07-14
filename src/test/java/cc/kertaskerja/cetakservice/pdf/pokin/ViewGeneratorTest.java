package cc.kertaskerja.cetakservice.pdf.pokin;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewGeneratorTest {

    @Test
    void should_generate_page_plan_with_correct_ancestors() {

        // generate by sub tematik
        List<PagePlan> pages = getPagePlans();

        assertEquals(1, pages.size());

        assertEquals("Sub Tema A", pages.getFirst().current().namaPohon());
    }

    private static List<PagePlan> getPagePlans() {
        Node strategic1 = new Node(
                4,
                2,
                2,
                JenisPohon.STRATEGIC_PEMDA,
                "Strategic 1",
                null,
                new ArrayList<>());

        Node strategic2 = new Node(
                5,
                2,
                2,
                JenisPohon.STRATEGIC_PEMDA,
                "Strategic 2",
                null,
                new ArrayList<>());

        Node subTema = new Node(
                2,
                1,
                1,
                JenisPohon.SUB_TEMATIK,
                "Sub Tema A",
                null,
                new ArrayList<>(List.of(strategic1, strategic2)));

        Node tema = new Node(
                1,
                0,
                0,
                JenisPohon.TEMATIK,
                "Tema",
                null,
                new ArrayList<>(List.of(subTema)));

        ViewGenerator generator = new ViewGenerator();

        return generator.generate(tema, ViewMode.PEMDA);
    }

}