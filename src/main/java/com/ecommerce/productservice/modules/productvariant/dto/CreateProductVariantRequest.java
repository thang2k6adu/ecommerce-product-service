package com.ecommerce.productservice.modules.productvariant.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductVariantRequest {

    @NotBlank(message = "SKU is required")
    private String sku;

    private String size;

    private String color;

    private String material;

    private String style;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    private BigDecimal priceAdjustment;

    private String imageUrl;

    private BigDecimal weight;

    private String barcode;

    private Boolean available;
}
