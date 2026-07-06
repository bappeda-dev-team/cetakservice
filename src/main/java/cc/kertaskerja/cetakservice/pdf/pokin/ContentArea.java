package cc.kertaskerja.cetakservice.pdf.pokin;

// Area menggambar content (misal: grafik pokin)
public record ContentArea(
        float x,
        float y,
        float width,
        float height
) {
    public float right() {
        return x + width;
    }

    public float top() {
        return y + height;
    }
}
