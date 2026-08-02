package cc.kertaskerja.cetakservice.pdf.pokin;

import cc.kertaskerja.cetakservice.pdf.TextUtils;

import java.io.IOException;

import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.BOX_BODY_FONT;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.BOX_FONT_SIZE;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.BOX_HEADER_HEIGHT;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.BOX_HEIGHT;
import static cc.kertaskerja.cetakservice.pdf.pokin.LayoutConstant.BOX_WIDTH;

/** Perhitungan ukuran kotak yang dipakai bersama oleh layout dan renderer. */
final class NodeSizeCalculator {
    static final float CROSSCUTTING_BOX_HEIGHT = 200f;
    private static final float TEXT_PADDING = 6f;
    private static final float TEXT_LINE_HEIGHT = BOX_FONT_SIZE * 1.2f;
    private static final float MIN_TUJUAN_BODY_HEIGHT = BOX_HEIGHT - BOX_HEADER_HEIGHT;

    private NodeSizeCalculator() {
    }

    static NodeSize getNodeSize(Node node) {
        if (hasCrosscutting(node)) {
            return new NodeSize(BOX_WIDTH, CROSSCUTTING_BOX_HEIGHT);
        }

        if (hasTujuanOpd(node)) {
            return new NodeSize(BOX_WIDTH, BOX_HEADER_HEIGHT + tujuanBodyHeight(node));
        }

        if (hasIndikatorPokins(node)) {
            int targetCount = node.nodeMetadata().indikatorPokins().stream()
                    .mapToInt(indikator -> indikator.targets().size())
                    .sum();
            return new NodeSize(BOX_WIDTH,
                    BOX_HEIGHT + 45f * node.nodeMetadata().indikatorPokins().size() + 22f * targetCount);
        }

        return new NodeSize(BOX_WIDTH, BOX_HEIGHT);
    }

    static float tujuanBodyHeight(Node node) {
        float contentHeight = (float) node.nodeMetadata().tujuanOpds().stream()
                .mapToDouble(tujuan -> tujuanItemHeight(tujuan.namaTujuan()))
                .sum();
        return Math.max(MIN_TUJUAN_BODY_HEIGHT, contentHeight);
    }

    static float tujuanItemHeight(String text) {
        try {
            int lineCount = Math.max(1, TextUtils.wrapText(
                    text == null ? "" : text,
                    BOX_BODY_FONT,
                    BOX_FONT_SIZE,
                    BOX_WIDTH - TEXT_PADDING * 2).size());
            // Tambahan 1pt mencegah pembulatan floating-point membuat satu baris terpotong.
            return Math.max(30f, TEXT_PADDING * 2 + lineCount * TEXT_LINE_HEIGHT + 1f);
        } catch (IOException exception) {
            throw new IllegalStateException("Tidak dapat menghitung ukuran teks tujuan OPD", exception);
        }
    }

    private static boolean hasCrosscutting(Node node) {
        return node.nodeMetadata() != null && node.nodeMetadata().isCrosscutting()
                && !node.nodeMetadata().crosscuttingPokins().isEmpty();
    }

    private static boolean hasTujuanOpd(Node node) {
        return node.nodeMetadata() != null && node.nodeMetadata().tujuanOpds() != null
                && !node.nodeMetadata().tujuanOpds().isEmpty();
    }

    private static boolean hasIndikatorPokins(Node node) {
        return node.nodeMetadata() != null && node.nodeMetadata().indikatorPokins() != null
                && !node.nodeMetadata().indikatorPokins().isEmpty();
    }
}
