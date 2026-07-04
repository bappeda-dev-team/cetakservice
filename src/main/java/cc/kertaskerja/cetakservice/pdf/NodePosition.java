package cc.kertaskerja.cetakservice.pdf;

public record NodePosition(
        float x,
        float y,
        float width,
        float height
) {

    public float left() {
        return x;
    }

    public float right() {
        return x + width;
    }

    public float top() {
        return y + height;
    }

    public float bottom() {
        return y;
    }

    public float centerX() {
        return x + width / 2;
    }

    public float centerY() {
        return y + height / 2;
    }

    public float topCenterX() {
        return centerX();
    }

    public float topCenterY() {
        return top();
    }

    public float bottomCenterX() {
        return centerX();
    }

    public float bottomCenterY() {
        return bottom();
    }
}
