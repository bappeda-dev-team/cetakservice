package cc.kertaskerja.cetakservice.common;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalStorageService {
    private static final Path ROOT = Path.of("storage", "pdf");

    public void save(String fileName, byte[] content) {

        try {
            Files.createDirectories(ROOT);

            Path file = ROOT.resolve(fileName);

            Files.write(file, content);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
