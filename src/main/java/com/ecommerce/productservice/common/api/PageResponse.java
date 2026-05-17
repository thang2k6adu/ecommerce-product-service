package com.ecommerce.productservice.common.api;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {

    private List<T> content;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private Boolean last;
    private Boolean first;

    public PageMeta toMeta() {
        return PageMeta.builder()
                .page(page != null ? page : 0)
                .size(size != null ? size : 0)
                .totalElements(totalElements != null ? totalElements : 0L)
                .totalPages(totalPages != null ? totalPages : 0)
                .first(first)
                .last(last)
                .build();
    }
}
