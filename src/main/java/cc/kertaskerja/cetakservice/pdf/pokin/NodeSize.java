package cc.kertaskerja.cetakservice.pdf.pokin;

public record NodeSize(
        float width,
        float height
) {
    public static NodeSize from(Node node) {
        NodeMetadata metadata = node.nodeMetadata();

        if (metadata != null
                && metadata.isCrosscutting()
                && metadata.crosscuttingPokins() != null
                && !metadata.crosscuttingPokins().isEmpty()) {
            return new NodeSize(
                    LayoutConstant.BOX_WIDTH,
                    LayoutConstant.CROSSCUTTING_BOX_HEIGHT);
        }

        if (metadata != null
                && metadata.tujuanOpds() != null
                && !metadata.tujuanOpds().isEmpty()) {
            return new NodeSize(
                    LayoutConstant.BOX_WIDTH,
                    LayoutConstant.TUJUAN_OPD_BOX_HEIGHT);
        }

        return new NodeSize(
                LayoutConstant.BOX_WIDTH,
                LayoutConstant.BOX_HEIGHT);
    }
}
