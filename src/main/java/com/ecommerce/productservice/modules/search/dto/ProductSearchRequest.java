package com.ecommerce.productservice.modules.search.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchRequest {

    private String keyword;

    private UUID categoryId;

    private UUID brandId;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Boolean featured;

    private String sortBy;

    private String sortDirection;

    private Integer page;

    private Integer size;
}
