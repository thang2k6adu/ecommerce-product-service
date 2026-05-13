package com.ecommerce.productservice.modules.cart.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class CartCatalogProduct {

    private final String id;
    private final String name;
    private final BigDecimal price;
    private final BigDecimal salePrice;
    private final Integer stockQuantity;
    private final String imageUrl;

    public BigDecimal effectiveUnitPrice() {
        return salePrice != null ? salePrice : price;
    }
}
