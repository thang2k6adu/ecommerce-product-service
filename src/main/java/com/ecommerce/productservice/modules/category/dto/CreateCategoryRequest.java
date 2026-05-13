package com.ecommerce.productservice.modules.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must not exceed 100 characters")
    private String name;

    @Size(max = 120, message = "Slug must not exceed 120 characters")
    private String slug;

    private String description;

    private UUID parentId;

    private Boolean active;

    private Integer displayOrder;

    private String imageUrl;

    @Size(max = 150, message = "Meta title must not exceed 150 characters")
    private String metaTitle;

    @Size(max = 300, message = "Meta description must not exceed 300 characters")
    private String metaDescription;

    @Size(max = 200, message = "Meta keywords must not exceed 200 characters")
    private String metaKeywords;
}
