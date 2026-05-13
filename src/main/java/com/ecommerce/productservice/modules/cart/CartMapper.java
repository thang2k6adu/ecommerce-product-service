package com.ecommerce.productservice.modules.cart;

import com.ecommerce.productservice.modules.cart.dto.response.CartItemResponse;
import com.ecommerce.productservice.modules.cart.dto.response.CartResponse;
import com.ecommerce.productservice.modules.cart.entity.Cart;
import com.ecommerce.productservice.modules.cart.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "status", expression = "java(cart.getStatus().name())")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "totalItems", expression = "java(calculateTotalItems(cart))")
    @Mapping(target = "subtotal", expression = "java(calculateSubtotal(cart))")
    @Mapping(target = "discount", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "total", expression = "java(calculateSubtotal(cart))")
    CartResponse toResponse(Cart cart);

    @Mapping(target = "total", expression = "java(calculateItemTotal(item))")
    CartItemResponse toItemResponse(CartItem item);

    List<CartItemResponse> toItemResponseList(List<CartItem> items);

    default Integer calculateTotalItems(Cart cart) {
        if (cart == null || cart.getItems() == null) {
            return 0;
        }
        return cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    default BigDecimal calculateSubtotal(Cart cart) {
        if (cart == null || cart.getItems() == null) {
            return BigDecimal.ZERO;
        }
        return cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    default BigDecimal calculateItemTotal(CartItem item) {
        if (item == null || item.getPrice() == null) {
            return BigDecimal.ZERO;
        }
        return item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }
}
