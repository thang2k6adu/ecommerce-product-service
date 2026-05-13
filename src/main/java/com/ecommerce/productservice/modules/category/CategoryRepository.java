package com.ecommerce.productservice.modules.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    Optional<CategoryEntity> findBySlug(String slug);

    List<CategoryEntity> findByParentIsNull();

    List<CategoryEntity> findByParentId(UUID parentId);

    List<CategoryEntity> findByActiveTrue();

    @Query("SELECT c FROM CategoryEntity c WHERE c.parent IS NULL AND c.active = true ORDER BY c.displayOrder ASC, c.name ASC")
    List<CategoryEntity> findRootCategoriesActive();

    boolean existsBySlug(String slug);
}
