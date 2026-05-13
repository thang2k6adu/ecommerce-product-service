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
public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {

    Optional<ProductEntity> findBySlug(String slug);

    Page<ProductEntity> findByPublishedTrue(Pageable pageable);

    Page<ProductEntity> findByCategoryId(UUID categoryId, Pageable pageable);

    Page<ProductEntity> findByBrandId(UUID brandId, Pageable pageable);

    Page<ProductEntity> findByFeaturedTrue(Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.published = true AND p.category.id = :categoryId")
    Page<ProductEntity> findPublishedByCategoryId(@Param("categoryId") UUID categoryId, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.published = true AND p.brand.id = :brandId")
    Page<ProductEntity> findPublishedByBrandId(@Param("brandId") UUID brandId, Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.published = true AND p.price BETWEEN :minPrice AND :maxPrice")
    Page<ProductEntity> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                    @Param("maxPrice") BigDecimal maxPrice,
                                    Pageable pageable);

    @Query("SELECT p FROM ProductEntity p WHERE p.published = true AND p.status = :status")
    Page<ProductEntity> findByStatus(@Param("status") ProductStatus status, Pageable pageable);

    boolean existsBySlug(String slug);

    @Query("SELECT DISTINCT p FROM ProductEntity p LEFT JOIN FETCH p.images LEFT JOIN FETCH p.category LEFT JOIN FETCH p.brand WHERE p.id = :id")
    Optional<ProductEntity> findByIdWithDetails(@Param("id") UUID id);
}
