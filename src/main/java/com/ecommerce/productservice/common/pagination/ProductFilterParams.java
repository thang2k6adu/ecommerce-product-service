package com.ecommerce.productservice.common.pagination;

import com.ecommerce.productservice.modules.product.ProductStatus;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ParameterObject
public class ProductFilterParams extends PageParams {

    private String keyword;
    private UUID categoryId;
    private UUID brandId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean featured;
    private Boolean published;
    private ProductStatus status;
}
