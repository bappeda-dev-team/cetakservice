package cc.kertaskerja.cetakservice.pdf.pokin;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.PAGE_MARGIN_BOTTOM;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.PAGE_HEADER_HEIGHT;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.PAGE_MARGIN_TOP;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.PAPER_MARGIN_TOP;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.TITLE_PAGE_PADDING;

/** Membagi pohon tinggi menjadi beberapa halaman tanpa memotong kotak node. */
@Component
public class LayoutPaginator {

    public List<PageSegment> paginate(LayoutResult layout, PDRectangle pageSize) {
        float contentStartY = PAGE_MARGIN_TOP + PAGE_HEADER_HEIGHT + TITLE_PAGE_PADDING + PAPER_MARGIN_TOP - 5f;
        float pageCapacity = pageSize.getHeight() - contentStartY - PAGE_MARGIN_BOTTOM;
        List<NodeRange> ranges = nodeRanges(layout.root());
        float treeBottom = ranges.stream().map(NodeRange::bottom).max(Float::compare).orElse(0f);

        List<Float> starts = new ArrayList<>();
        float start = 0f;
        while (start < treeBottom) {
            starts.add(start);
            float pageStart = start;
            float limit = pageStart + pageCapacity;
            float nextStart = ranges.stream()
                    .filter(range -> range.top() >= pageStart && range.bottom() > limit)
                    .map(NodeRange::top)
                    .min(Float::compare)
                    .orElse(treeBottom);

            // Node tunggal yang lebih tinggi dari halaman tetap harus dirender;
            // ini mencegah loop tak berujung pada data yang sangat panjang.
            start = nextStart > pageStart ? nextStart : limit;
        }

        int total = starts.size();
        List<PageSegment> result = new ArrayList<>(total);
        for (int index = 0; index < total; index++) {
            result.add(new PageSegment(starts.get(index), index + 1, total));
        }
        return result;
    }

    private List<NodeRange> nodeRanges(LayoutNode root) {
        List<NodeRange> ranges = new ArrayList<>();
        collectRanges(root, ranges);
        ranges.sort(Comparator.comparing(NodeRange::top));
        return ranges;
    }

    private void collectRanges(LayoutNode node, List<NodeRange> ranges) {
        ranges.add(new NodeRange(node.getY(), node.getY() + NodeSizeCalculator.getNodeSize(node.getNode()).height()));
        for (LayoutNode child : node.getChildren()) {
            collectRanges(child, ranges);
        }
    }

    private record NodeRange(float top, float bottom) {
    }
}
