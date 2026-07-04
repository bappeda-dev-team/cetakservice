package cc.kertaskerja.cetakservice.pdf;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

public enum PageOrientation {

    PORTRAIT {
        @Override
        public PDRectangle createRectangle(PDRectangle base) {
            return base;
        }
    },

    LANDSCAPE {
        @Override
        public PDRectangle createRectangle(PDRectangle base) {
            return new PDRectangle(base.getHeight(), base.getWidth());
        }
    };

    public abstract PDRectangle createRectangle(PDRectangle base);
}
