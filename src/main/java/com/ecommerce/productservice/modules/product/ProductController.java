package com.ecommerce.productservice.modules.product;

import com.ecommerce.productservice.common.api.ApiResponse;
import com.ecommerce.productservice.common.api.PageResponse;
import com.ecommerce.productservice.common.pagination.PageParams;
import com.ecommerce.productservice.modules.product.dto.CreateProductRequest;
import com.ecommerce.productservice.modules.product.dto.ProductResponse;
import com.ecommerce.productservice.modules.productimage.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ImageService imageService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable UUID id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductBySlug(@PathVariable String slug) {
        ProductResponse response = productService.getProductBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(
            @ParameterObject PageParams pageParams) {
        PageResponse<ProductResponse> response = productService.getAllProducts(pageParams);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/published")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getPublishedProducts(
            @ParameterObject PageParams pageParams) {
        PageResponse<ProductResponse> response = productService.getPublishedProducts(pageParams);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCategory(
            @PathVariable UUID categoryId,
            @ParameterObject PageParams pageParams) {
        PageResponse<ProductResponse> response = productService.getProductsByCategory(categoryId, pageParams);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByBrand(
            @PathVariable UUID brandId,
            @ParameterObject PageParams pageParams) {
        PageResponse<ProductResponse> response = productService.getProductsByBrand(brandId, pageParams);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getFeaturedProducts(
            @ParameterObject PageParams pageParams) {
        PageResponse<ProductResponse> response = productService.getFeaturedProducts(pageParams);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @ParameterObject PageParams pageParams) {
        PageResponse<ProductResponse> response = productService.getProductsByPriceRange(minPrice, maxPrice, pageParams);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }

    @PostMapping("/{id}/images/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> uploadProductImage(
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {
        String imageUrl = imageService.uploadProductImage(file, id);
        return ResponseEntity.ok(ApiResponse.success("Image uploaded successfully", imageUrl));
    }

    @PostMapping("/sync-elasticsearch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> syncAllProductsToElasticsearch() {
        productService.syncAllProductsToElasticsearch();
        return ResponseEntity.ok(ApiResponse.success("All products synced to Elasticsearch", null));
    }
}
