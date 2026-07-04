package cc.kertaskerja.cetakservice.client.perencanaan.domain;

public record PerencanaanWebResponse<T>(
        Integer code,
        String status,
        String error,
        T data
) {
}
