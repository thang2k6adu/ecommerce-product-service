package com.ecommerce.productservice.modules.product;

import com.ecommerce.productservice.common.pagination.ProductFilterParams;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public final class ProductSpecifications {

    private ProductSpecifications() {
    }

    public static Specification<ProductEntity> from(ProductFilterParams params) {
        if (params == null) {
            return Specification.where(null);
        }
        return Specification
                .where(matchesKeyword(params.getKeyword()))
                .and(hasCategoryId(params.getCategoryId()))
                .and(hasBrandId(params.getBrandId()))
                .and(hasMinPrice(params.getMinPrice()))
                .and(hasMaxPrice(params.getMaxPrice()))
                .and(hasFeatured(params.getFeatured()))
                .and(hasPublished(params.getPublished()))
                .and(hasStatus(params.getStatus()));
    }

    private static Specification<ProductEntity> matchesKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        String pattern = "%" + keyword.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), pattern),
                cb.like(cb.lower(root.get("slug")), pattern),
                cb.and(
                        cb.isNotNull(root.get("sku")),
                        cb.like(cb.lower(root.get("sku")), pattern)));
    }

    private static Specification<ProductEntity> hasCategoryId(UUID categoryId) {
        if (categoryId == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }

    private static Specification<ProductEntity> hasBrandId(UUID brandId) {
        if (brandId == null) {
            return null;
        }
        // TƯơng đương Where brand.id = :brandId, cb sẽ add điều kiênj này vào
        return (root, query, cb) -> cb.equal(root.get("brand").get("id"), brandId);
    }

    private static Specification<ProductEntity> hasMinPrice(BigDecimal minPrice) {
        if (minPrice == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    private static Specification<ProductEntity> hasMaxPrice(BigDecimal maxPrice) {
        if (maxPrice == null) {
            return null;
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    private static Specification<ProductEntity> hasFeatured(Boolean featured) {
        if (featured == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("featured"), featured);
    }

    private static Specification<ProductEntity> hasPublished(Boolean published) {
        if (published == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("published"), published);
    }

    private static Specification<ProductEntity> hasStatus(ProductStatus status) {
        if (status == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
