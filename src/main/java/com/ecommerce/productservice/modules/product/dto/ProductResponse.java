package com.ecommerce.productservice.modules.product.dto;

import com.ecommerce.productservice.modules.brand.dto.BrandResponse;
import com.ecommerce.productservice.modules.category.dto.CategoryResponse;
import com.ecommerce.productservice.modules.product.ProductStatus;
import com.ecommerce.productservice.modules.productimage.dto.ProductImageResponse;
import com.ecommerce.productservice.modules.productvariant.dto.ProductVariantResponse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String shortDescription;
    private BigDecimal price;
    private BigDecimal compareAtPrice;
    private BigDecimal costPrice;
    private CategoryResponse category;
    private BrandResponse brand;
    private ProductStatus status;
    private Boolean published;
    private Boolean featured;
    private Integer stockQuantity;
    private String sku;
    private String barcode;
    private BigDecimal weight;
    private String weightUnit;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private String dimensionUnit;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private List<ProductImageResponse> images;
    private List<ProductVariantResponse> variants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
}
