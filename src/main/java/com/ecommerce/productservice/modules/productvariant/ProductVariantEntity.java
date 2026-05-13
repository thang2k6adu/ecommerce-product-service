package com.ecommerce.productservice.modules.productvariant;

import com.ecommerce.productservice.modules.product.ProductEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_variants", indexes = {
        @Index(name = "idx_variant_product", columnList = "product_id"),
        @Index(name = "idx_variant_sku", columnList = "sku", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false, unique = true, length = 100)
    private String sku;

    @Column(length = 50)
    private String size;

    @Column(length = 50)
    private String color;

    @Column(length = 50)
    private String material;

    @Column(length = 100)
    private String style;

    @Column(nullable = false)
    @Builder.Default
    private Integer stockQuantity = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal priceAdjustment;

    private String imageUrl;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    private String barcode;

    @Column(nullable = false)
    @Builder.Default
    private Boolean available = true;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
