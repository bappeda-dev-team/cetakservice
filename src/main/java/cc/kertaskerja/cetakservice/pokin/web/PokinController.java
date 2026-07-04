package cc.kertaskerja.cetakservice.pokin.web;

import cc.kertaskerja.cetakservice.pokin.domain.PokinService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import cc.kertaskerja.cetakservice.common.web.WebResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/pokin")
public class PokinController {
    private final PokinService pokinService;

    public PokinController(PokinService pokinService) {
        this.pokinService = pokinService;
    }

    @PostMapping("/pemda")
    @Operation(summary = "Cetak pokin pemda",
            description = "Cetak pokin pemda dari tematik hingga operasional mengembalikan url download pdf pokin pemda")
    public WebResponse<String> cetakPokinPemda(
            @RequestBody
            @Valid
            CetakPokinPemdaRequest request
    ) {
        String url = pokinService.cetakPokinPemda(request.pokinId());
        return WebResponse.success(url);
    }

    @GetMapping("/opd")
    @Operation(summary = "Cetak pokin opd", description = "Cetak pokin opd by kode opd and tahun")
    public WebResponse<String> cetakPokinOpd(
            @Parameter(description = "kode opd", example = "1.02.03") String kodeOpd,
            @Parameter(description = "tahun", example = "2026") String tahun) {

        return WebResponse.success("CETAK POKIN OPD");

    }

}
