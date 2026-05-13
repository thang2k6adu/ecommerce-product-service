package com.ecommerce.productservice.modules.cart;

import com.ecommerce.productservice.modules.cart.catalog.CartCatalogProduct;
import com.ecommerce.productservice.modules.cart.catalog.CartProductCatalogService;
import com.ecommerce.productservice.modules.cart.entity.Cart;
import com.ecommerce.productservice.modules.cart.entity.CartItem;
import com.ecommerce.productservice.modules.cart.repository.CartItemRepository;
import com.ecommerce.productservice.common.exception.BadRequestException;
import com.ecommerce.productservice.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartProductCatalogService cartProductCatalogService;

    @Value("${cart.max-items-per-cart:50}")
    private Integer maxItemsPerCart;

    public CartItem addOrUpdateItem(Cart cart, String productId, String variantId, Integer quantity) {
        log.debug("Adding/updating item to cart: productId={}, variantId={}, quantity={}", productId, variantId, quantity);

        CartCatalogProduct product = cartProductCatalogService.getProduct(productId);
        if (product == null) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        if (!hasSufficientStock(product, quantity)) {
            log.warn("Stock check failed for product {}: requestedQuantity={}, stockQuantity={}",
                    productId, quantity, product.getStockQuantity());
            throw new BadRequestException("Product is out of stock");
        }

        CartItem existingItem;
        if (variantId != null) {
            existingItem = cartItemRepository.findByCart_IdAndProductIdAndProductVariantId(cart.getId(), productId, variantId)
                    .orElse(null);
        } else {
            existingItem = cartItemRepository.findByCart_IdAndProductId(cart.getId(), productId)
                    .orElse(null);
        }

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantity;

            if (newQuantity > product.getStockQuantity()) {
                throw new BadRequestException("Requested quantity exceeds available stock");
            }

            existingItem.setQuantity(newQuantity);
            existingItem.setPrice(product.effectiveUnitPrice());
            existingItem.setProductName(product.getName());
            existingItem.setProductImageUrl(product.getImageUrl());

            log.debug("Updated existing cart item: {}", existingItem.getId());
            return cartItemRepository.save(existingItem);
        } else {
            int maxItemsLimit = maxItemsPerCart != null ? maxItemsPerCart : 50;
            Integer currentItemCount = cartItemRepository.getTotalItemsByCartId(cart.getId());
            if (currentItemCount != null && currentItemCount >= maxItemsLimit) {
                throw new BadRequestException("Cart has reached maximum items limit of " + maxItemsLimit);
            }

            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productId(productId)
                    .productVariantId(variantId)
                    .quantity(quantity)
                    .price(product.effectiveUnitPrice())
                    .productName(product.getName())
                    .productImageUrl(product.getImageUrl())
                    .build();

            cart.addItem(newItem);

            log.debug("Created new cart item for product: {}", productId);
            return cartItemRepository.save(newItem);
        }
    }

    public void removeItem(UUID cartItemId) {
        log.debug("Removing cart item: {}", cartItemId);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        cartItemRepository.delete(item);
        log.debug("Cart item removed: {}", cartItemId);
    }

    public void updateQuantity(UUID cartItemId, Integer quantity) {
        log.debug("Updating cart item quantity: itemId={}, quantity={}", cartItemId, quantity);

        if (quantity < 0) {
            throw new BadRequestException("Quantity cannot be negative");
        }

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (quantity == 0) {
            cartItemRepository.delete(item);
            log.debug("Cart item removed (quantity=0): {}", cartItemId);
        } else {
            if (!cartProductCatalogService.checkStock(item.getProductId(), quantity)) {
                throw new BadRequestException("Requested quantity exceeds available stock");
            }

            item.setQuantity(quantity);
            cartItemRepository.save(item);
            log.debug("Cart item quantity updated: {}", cartItemId);
        }
    }

    public List<CartItem> getCartItems(UUID cartId) {
        log.debug("Getting cart items for cart: {}", cartId);
        return cartItemRepository.findByCart_Id(cartId);
    }

    public CartItem getCartItemById(UUID cartItemId) {
        log.debug("Getting cart item by id: {}", cartItemId);
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));
    }

    public void validateCartItems(Cart cart) {
        log.debug("Validating cart items for cart: {}", cart.getId());

        List<CartItem> items = cart.getItems();
        boolean updated = false;

        for (CartItem item : items) {
            try {
                CartCatalogProduct product = cartProductCatalogService.getProduct(item.getProductId());

                if (isOutOfStock(product)) {
                    log.warn("Product {} is out of stock, will be marked", item.getProductId());
                    continue;
                }

                if (product.getStockQuantity() < item.getQuantity()) {
                    log.warn("Product {} has insufficient stock, adjusting quantity from {} to {}",
                            item.getProductId(), item.getQuantity(), product.getStockQuantity());
                    item.setQuantity(product.getStockQuantity());
                    updated = true;
                }

                BigDecimal currentPrice = product.effectiveUnitPrice();
                if (!item.getPrice().equals(currentPrice)) {
                    log.debug("Updating price for product {} from {} to {}",
                            item.getProductId(), item.getPrice(), currentPrice);
                    item.setPrice(currentPrice);
                    updated = true;
                }

            } catch (Exception e) {
                log.error("Error validating cart item {}: {}", item.getId(), e.getMessage());
            }
        }

        if (updated) {
            cartItemRepository.saveAll(items);
            log.debug("Cart items validated and updated");
        }
    }

    private boolean hasSufficientStock(CartCatalogProduct product, Integer quantity) {
        if (product == null || quantity == null || quantity <= 0) {
            return false;
        }

        Integer stockQuantity = product.getStockQuantity();
        return stockQuantity != null && stockQuantity != 0;
    }

    private boolean isOutOfStock(CartCatalogProduct product) {
        return product == null || product.getStockQuantity() == null || product.getStockQuantity() <= 0;
    }

    @Transactional
    public void deleteCartItemsByCartId(UUID cartId) {
        log.debug("Deleting all cart items for cart: {}", cartId);
        cartItemRepository.deleteByCartId(cartId);
        log.debug("All cart items deleted for cart: {}", cartId);
    }
}
