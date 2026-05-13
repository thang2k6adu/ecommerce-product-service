package com.ecommerce.productservice.modules.brand.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBrandRequest {

    @NotBlank(message = "Brand name is required")
    @Size(max = 100, message = "Brand name must not exceed 100 characters")
    private String name;

    @Size(max = 120, message = "Slug must not exceed 120 characters")
    private String slug;

    private String description;

    private String logoUrl;

    private String websiteUrl;

    private Boolean active;

    @Size(max = 150, message = "Meta title must not exceed 150 characters")
    private String metaTitle;

    @Size(max = 300, message = "Meta description must not exceed 300 characters")
    private String metaDescription;
}
