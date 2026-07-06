package cc.kertaskerja.cetakservice.pdf.pokin;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewGeneratorTest {

    @Test
    void should_generate_page_plan_with_correct_ancestors() {

        Node strategic1 = new Node(
                4,
                2,
                2,
                "Strategic",
                "Strategic 1",
                new ArrayList<>());

        Node strategic2 = new Node(
                5,
                2,
                2,
                "Strategic",
                "Strategic 2",
                new ArrayList<>());

        Node subTema = new Node(
                2,
                1,
                1,
                "Sub Tema",
                "Sub Tema A",
                new ArrayList<>(List.of(strategic1, strategic2)));

        Node tema = new Node(
                1,
                0,
                0,
                "Tema",
                "Tema",
                new ArrayList<>(List.of(subTema)));

        ViewGenerator generator = new ViewGenerator();

        List<PagePlan> pages = generator.generate(tema);

        assertEquals(4, pages.size());

        assertEquals("Tema", pages.get(0).current().namaPohon());
        assertTrue(pages.get(0).ancestors().isEmpty());

        assertEquals("Sub Tema A", pages.get(1).current().namaPohon());
        assertEquals(
                List.of("Tema"),
                pages.get(1).ancestors()
                        .stream()
                        .map(Node::namaPohon)
                        .toList()
        );

        assertEquals("Strategic 1", pages.get(2).current().namaPohon());
        assertEquals(
                List.of("Tema", "Sub Tema A"),
                pages.get(2).ancestors()
                        .stream()
                        .map(Node::namaPohon)
                        .toList()
        );

        assertEquals("Strategic 2", pages.get(3).current().namaPohon());
        assertEquals(
                List.of("Tema", "Sub Tema A"),
                pages.get(3).ancestors()
                        .stream()
                        .map(Node::namaPohon)
                        .toList()
        );
    }

}