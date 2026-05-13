package com.ecommerce.productservice.modules.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Optional<Product> findBySlug(String slug);

    Page<Product> findByPublishedTrue(Pageable pageable);

    Page<Product> findByCategoryId(UUID categoryId, Pageable pageable);

    Page<Product> findByBrandId(UUID brandId, Pageable pageable);

    Page<Product> findByFeaturedTrue(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.published = true AND p.category.id = :categoryId")
    Page<Product> findPublishedByCategoryId(@Param("categoryId") UUID categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.published = true AND p.brand.id = :brandId")
    Page<Product> findPublishedByBrandId(@Param("brandId") UUID brandId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.published = true AND p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                    @Param("maxPrice") BigDecimal maxPrice,
                                    Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.published = true AND p.status = :status")
    Page<Product> findByStatus(@Param("status") ProductStatus status, Pageable pageable);

    boolean existsBySlug(String slug);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.images LEFT JOIN FETCH p.category LEFT JOIN FETCH p.brand WHERE p.id = :id")
    Optional<Product> findByIdWithDetails(@Param("id") UUID id);
}
