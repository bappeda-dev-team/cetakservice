package cc.kertaskerja.cetakservice.storage;

public interface PdfStorageService {
    String storePdf(String fileName, byte[] pdf);
}
