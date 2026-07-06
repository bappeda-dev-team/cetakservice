package cc.kertaskerja.cetakservice.pdf.pokin;

public record LayoutBound(
        float minX,
        float maxX,
        float minY,
        float maxY
) {

    public float width() {
        return maxX - minX;
    }

    public float height() {
        return maxY - minY;
    }
}
