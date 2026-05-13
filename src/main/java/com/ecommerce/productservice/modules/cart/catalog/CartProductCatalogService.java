package com.ecommerce.productservice.modules.cart.catalog;

import com.ecommerce.productservice.common.exception.ResourceNotFoundException;
import com.ecommerce.productservice.modules.product.ProductService;
import com.ecommerce.productservice.modules.product.dto.ProductResponse;
import com.ecommerce.productservice.modules.productimage.dto.ProductImageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartProductCatalogService {

    private final ProductService productService;

    public CartCatalogProduct getProduct(String productId) {
        try {
            UUID id = UUID.fromString(productId.trim());
            ProductResponse product = productService.getProductById(id);
            return map(product);
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
    }

    public boolean checkStock(String productId, Integer quantity) {
        try {
            log.debug("Checking stock for product {} with quantity {}", productId, quantity);

            if (quantity == null || quantity <= 0) {
                return false;
            }

            CartCatalogProduct product = getProduct(productId);

            Integer stockQuantity = product.getStockQuantity();
            log.debug("Resolved stock for product {}: stockQuantity={}", productId, stockQuantity);

            return stockQuantity != null && stockQuantity != 0;
        } catch (Exception e) {
            log.error("Failed to check stock for product {}: {}", productId, e.getMessage());
            return false;
        }
    }

    public Map<String, CartCatalogProduct> getProductsByIds(List<String> productIds) {
        Map<String, CartCatalogProduct> productMap = new HashMap<>();

        if (productIds == null || productIds.isEmpty()) {
            return productMap;
        }

        log.debug("Fetching multiple products: {}", productIds);

        for (String productId : productIds) {
            try {
                CartCatalogProduct product = getProduct(productId);
                if (product != null) {
                    productMap.put(productId, product);
                }
            } catch (Exception e) {
                log.error("Failed to fetch product {} in batch: {}", productId, e.getMessage());
            }
        }

        return productMap;
    }

    private CartCatalogProduct map(ProductResponse p) {
        String imageUrl = resolvePrimaryImageUrl(p);
        return CartCatalogProduct.builder()
                .id(p.getId().toString())
                .name(p.getName())
                .price(p.getPrice())
                .salePrice(null)
                .stockQuantity(p.getStockQuantity())
                .imageUrl(imageUrl)
                .build();
    }

    private static String resolvePrimaryImageUrl(ProductResponse p) {
        if (p.getImages() == null || p.getImages().isEmpty()) {
            return null;
        }
        return p.getImages().stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                .map(ProductImageResponse::getImageUrl)
                .findFirst()
                .orElseGet(() -> p.getImages().stream()
                        .min(Comparator.comparingInt(i -> i.getDisplayOrder() != null ? i.getDisplayOrder() : Integer.MAX_VALUE))
                        .map(ProductImageResponse::getImageUrl)
                        .orElse(p.getImages().get(0).getImageUrl()));
    }
}
