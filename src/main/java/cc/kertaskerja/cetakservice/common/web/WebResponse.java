package cc.kertaskerja.cetakservice.common.web;

import org.springframework.http.HttpStatus;

public record WebResponse<T>(
        int code,
        String status,
        String message,
        T data) {

    public static <T> WebResponse<T> success(T data) {
        return new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                "Success",
                data);
    }

    public static <T> WebResponse<T> success(String message, T data) {
        return new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                message,
                data);
    }

    public static <T> WebResponse<T> created(String message, T data) {
        return new WebResponse<>(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                message,
                data);
    }

    public static WebResponse<Void> deleted(String message) {
        return new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                message,
                null);
    }

    public static WebResponse<Void> badRequest(String message) {
        return new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                message,
                null);
    }
}
