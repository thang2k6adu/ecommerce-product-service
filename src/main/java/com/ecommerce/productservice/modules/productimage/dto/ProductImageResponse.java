package com.ecommerce.productservice.modules.productimage.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageResponse {

    private UUID id;
    private String imageUrl;
    private String altText;
    private Boolean isPrimary;
    private Integer displayOrder;
    private LocalDateTime createdAt;
}
