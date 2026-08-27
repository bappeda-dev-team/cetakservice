package cc.kertaskerja.cetakservice.pdf.pokin;

public record RenderPage(
        String judulHalaman,
        String title,
        String subTitle,
        LayoutResult layout,
        float verticalOffset
) {
    public RenderPage(String judulHalaman, String title, String subTitle, LayoutResult layout) {
        this(judulHalaman, title, subTitle, layout, 0f);
    }
}
