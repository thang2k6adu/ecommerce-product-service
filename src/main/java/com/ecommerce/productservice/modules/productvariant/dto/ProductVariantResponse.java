package com.ecommerce.productservice.modules.productvariant.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantResponse {

    private UUID id;
    private String sku;
    private String size;
    private String color;
    private String material;
    private String style;
    private Integer stockQuantity;
    private BigDecimal priceAdjustment;
    private String imageUrl;
    private BigDecimal weight;
    private String barcode;
    private Boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
