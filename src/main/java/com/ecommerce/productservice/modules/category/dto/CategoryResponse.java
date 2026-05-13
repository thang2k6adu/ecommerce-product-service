package com.ecommerce.productservice.modules.category.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private UUID id;
    private String name;
    private String slug;
    private String description;
    private UUID parentId;
    private String parentName;
    private List<CategoryResponse> children;
    private Boolean active;
    private Integer displayOrder;
    private String imageUrl;
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
