package com.ecommerce.productservice.modules.cart.repository;

import com.ecommerce.productservice.modules.cart.entity.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    List<CartItem> findByCart_Id(UUID cartId);

    Page<CartItem> findByCart_Id(UUID cartId, Pageable pageable);

    Optional<CartItem> findByCart_IdAndProductId(UUID cartId, String productId);

    Optional<CartItem> findByCart_IdAndProductIdAndProductVariantId(UUID cartId, String productId, String variantId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId")
    void deleteByCartId(@Param("cartId") UUID cartId);

    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.cart.id = :cartId")
    Integer getTotalItemsByCartId(@Param("cartId") UUID cartId);

    @Query("SELECT COALESCE(SUM(ci.price * ci.quantity), 0) FROM CartItem ci WHERE ci.cart.id = :cartId")
    BigDecimal getSubtotalByCartId(@Param("cartId") UUID cartId);
}
