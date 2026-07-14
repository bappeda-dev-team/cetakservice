package cc.kertaskerja.cetakservice.common.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@RequestMapping("/dev")
@Profile("dev")
public class DevController {
    // CONTROLLER UNTUK TEST HASIL PDF

    @GetMapping(
            value = "/result/**",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<Resource> latestPokin(HttpServletRequest request) {

        String path = request.getRequestURI()
                .replaceFirst("/dev/result/", "");

        Path file = Path.of("storage", "pdf").resolve(path);


        Resource resource = new FileSystemResource(file);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
