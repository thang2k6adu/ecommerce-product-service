package com.ecommerce.productservice.common.pagination;

import java.util.Set;

public final class SortFields {

    public static final Set<String> BRAND = Set.of(
            "name", "slug", "active", "createdAt", "updatedAt"
    );

    public static final Set<String> CATEGORY = Set.of(
            "name", "slug", "displayOrder", "active", "createdAt", "updatedAt"
    );

    public static final Set<String> PRODUCT = Set.of(
            "name", "slug", "price", "stockQuantity", "published", "featured",
            "createdAt", "updatedAt", "status"
    );

    public static final Set<String> CART_ITEM = Set.of(
            "createdAt", "updatedAt", "price", "quantity", "productName"
    );

    public static final Set<String> PRODUCT_DOCUMENT = PRODUCT;

    private SortFields() {
    }
}
