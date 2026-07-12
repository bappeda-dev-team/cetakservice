package cc.kertaskerja.cetakservice.common.web;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
@RequestMapping("/dev")
@Profile("dev")
public class DevController {
    // CONTROLLER UNTUK TEST HASIL PDF

    @GetMapping(
            value = "/latest-pokin",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<Resource> latestPokin() {

        Path path = Path.of("storage/pdf/latest-pokin.pdf");

        Resource resource = new FileSystemResource(path);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping(
            value = "/latest-pokin-opd",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<Resource> latestPokinOpd() {

        Path path = Path.of("storage/pdf/latest-pokin-opd.pdf");

        Resource resource = new FileSystemResource(path);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

}
