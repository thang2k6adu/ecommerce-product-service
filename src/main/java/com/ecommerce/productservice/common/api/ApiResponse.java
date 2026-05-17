package com.ecommerce.productservice.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.Instant;
import java.util.List;

/**
 * Aligns with REACT-FASHION {@code ApiResponse<T, M>} / {@code PageMeta}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"success", "message", "data", "meta", "error", "timestamp"})
public class ApiResponse<T> {

    private Boolean success;
    private String message;
    private T data;
    private PageMeta meta;
    private String error;

    @Builder.Default
    private Instant timestamp = Instant.now();

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<List<T>> success(PageResponse<T> pageResponse) {
        return ApiResponse.<List<T>>builder()
                .success(true)
                .data(pageResponse.getContent())
                .meta(pageResponse.toMeta())
                .build();
    }

    public static <T> ApiResponse<T> successWithMeta(T data, PageMeta meta) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .meta(meta)
                .build();
    }

    public static <T> ApiResponse<T> error(String error) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(error)
                .build();
    }
}
