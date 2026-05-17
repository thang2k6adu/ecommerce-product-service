package com.ecommerce.productservice.modules.cart;

import com.ecommerce.productservice.common.api.ApiResponse;
import com.ecommerce.productservice.modules.cart.dto.request.AddToCartRequest;
import com.ecommerce.productservice.modules.cart.dto.request.MergeCartRequest;
import com.ecommerce.productservice.modules.cart.dto.request.UpdateCartItemRequest;
import com.ecommerce.productservice.modules.cart.dto.response.CartResponse;
import com.ecommerce.productservice.modules.cart.dto.response.CartSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cart Management", description = "APIs for managing shopping cart operations")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Get current cart")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<ApiResponse<CartResponse>> getCurrentCart(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            Authentication authentication) {
        log.debug("Getting current cart for user: {}", authentication.getName());
        var result = cartService.getCurrentCart(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.successWithMeta(result.getCart(), result.getItemsMeta()));
    }

    @Operation(summary = "Get guest cart")
    @GetMapping("/guest/{sessionId}")
    public ResponseEntity<ApiResponse<CartResponse>> getGuestCart(
            @Parameter(description = "Session ID of the guest cart") @PathVariable String sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        log.debug("Getting guest cart for session: {}", sessionId);
        var result = cartService.getGuestCart(sessionId, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(ApiResponse.successWithMeta(result.getCart(), result.getItemsMeta()));
    }

    @Operation(summary = "Add item to cart")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @Valid @RequestBody AddToCartRequest request,
            Authentication authentication) {
        log.debug("Adding item to cart: productId={}, quantity={}",
                request.getProductId(), request.getQuantity());

        CartResponse cart = cartService.addToCart(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item added to cart successfully", cart));
    }

    @Operation(summary = "Update cart item")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItem(
            @Parameter(description = "ID of the cart item to update") @PathVariable UUID itemId,
            @Valid @RequestBody UpdateCartItemRequest request,
            Authentication authentication) {
        log.debug("Updating cart item: itemId={}, quantity={}", itemId, request.getQuantity());

        CartResponse cart = cartService.updateCartItem(itemId, request);
        return ResponseEntity.ok(ApiResponse.success("Cart item updated successfully", cart));
    }

    @Operation(summary = "Remove cart item")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeCartItem(
            @Parameter(description = "ID of the cart item to remove") @PathVariable UUID itemId,
            Authentication authentication) {
        log.debug("Removing cart item: {}", itemId);

        cartService.removeCartItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Clear cart")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        log.debug("Clearing cart for user: {}", authentication.getName());

        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get cart summary")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<CartSummaryResponse>> getCartSummary(Authentication authentication) {
        log.debug("Getting cart summary for user: {}", authentication.getName());

        CartSummaryResponse summary = cartService.getCartSummary();
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    @Operation(summary = "Merge guest cart")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/merge")
    public ResponseEntity<ApiResponse<CartResponse>> mergeGuestCart(
            @Valid @RequestBody MergeCartRequest request,
            Authentication authentication) {
        log.debug("Merging guest cart to user cart: guestSessionId={}, userId={}",
                request.getGuestSessionId(), authentication.getName());

        String userId = authentication.getName();
        CartResponse cart = cartService.mergeGuestCart(request.getGuestSessionId(), userId);

        return ResponseEntity.ok(ApiResponse.success("Carts merged successfully", cart));
    }
}
