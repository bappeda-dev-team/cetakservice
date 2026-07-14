package cc.kertaskerja.cetakservice.common;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalStorageService {
    private static final Path ROOT = Path.of("storage", "pdf");

    public void save(String key, String fileName, byte[] content) {

        try {
            Path directory = ROOT.resolve(key);

            Files.createDirectories(directory);

            Files.write(
                    directory.resolve(fileName),
                    content
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
