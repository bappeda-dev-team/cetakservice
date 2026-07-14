package cc.kertaskerja.cetakservice.storage;

import cc.kertaskerja.cetakservice.client.upload.domain.UploadRequest;
import cc.kertaskerja.cetakservice.common.LocalStorageService;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Profile("dev")
public class LocalPdfStorageService implements PdfStorageService {

    private final LocalStorageService localStorageService;

    public LocalPdfStorageService(LocalStorageService localStorageService) {
        this.localStorageService = localStorageService;
    }

    @Override
    public Optional<String> findPdf(String key) {
        Path directory = Path.of("storage", "pdf").resolve(key);

        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            return Optional.empty();
        }

        try (Stream<Path> files = Files.list(directory)) {

            return files
                    .filter(Files::isRegularFile)
                    .findFirst()
                    .map(file -> "/dev/result/%s/%s".formatted(
                            key,
                            file.getFileName()
                    ));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String storePdf(
            byte[] pdf,
            String fileName,
            String category,
            String key
    ) {
        localStorageService.save(key, fileName, pdf);

        return "/dev/result/%s/%s".formatted(key, fileName);
    }
}
