package com.ecommerce.productservice.modules.product.dto;

import com.ecommerce.productservice.modules.product.ProductStatus;
import com.ecommerce.productservice.modules.productimage.dto.CreateProductImageRequest;
import com.ecommerce.productservice.modules.productvariant.dto.CreateProductVariantRequest;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 200, message = "Product name must not exceed 200 characters")
    private String name;

    @Size(max = 220, message = "Slug must not exceed 220 characters")
    private String slug;

    private String description;

    private String shortDescription;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    private BigDecimal compareAtPrice;

    private BigDecimal costPrice;

    private UUID categoryId;

    private UUID brandId;

    private ProductStatus status;

    private Boolean published;

    private Boolean featured;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @Size(max = 50, message = "SKU must not exceed 50 characters")
    private String sku;

    private String barcode;

    private BigDecimal weight;

    private String weightUnit;

    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;

    private String dimensionUnit;

    @Size(max = 150, message = "Meta title must not exceed 150 characters")
    private String metaTitle;

    @Size(max = 300, message = "Meta description must not exceed 300 characters")
    private String metaDescription;

    @Size(max = 200, message = "Meta keywords must not exceed 200 characters")
    private String metaKeywords;

    private List<CreateProductImageRequest> images;

    private List<CreateProductVariantRequest> variants;
}
