package com.ecommerce.productservice.modules.brand;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "brands", indexes = {
        @Index(name = "idx_brand_slug", columnList = "slug"),
        @Index(name = "idx_brand_active", columnList = "active")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 120)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String logoUrl;

    private String websiteUrl;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(length = 150)
    private String metaTitle;

    @Column(length = 300)
    private String metaDescription;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void generateSlug() {
        if (this.slug == null || this.slug.isEmpty()) {
            this.slug = createSlug(this.name);
        }
    }

    private String createSlug(String text) {
        return text.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }
}
