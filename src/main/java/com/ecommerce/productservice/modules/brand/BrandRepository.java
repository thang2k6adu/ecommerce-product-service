package com.ecommerce.productservice.modules.brand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, UUID> {

    Optional<BrandEntity> findBySlug(String slug);

    List<BrandEntity> findByActiveTrue();

    boolean existsBySlug(String slug);
}
