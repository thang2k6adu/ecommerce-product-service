package com.ecommerce.productservice.modules.search;

import com.ecommerce.productservice.common.api.ApiResponse;
import com.ecommerce.productservice.common.api.PageResponse;
import com.ecommerce.productservice.common.pagination.PageParams;
import com.ecommerce.productservice.document.ProductDocument;
import com.ecommerce.productservice.modules.search.dto.ProductSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ConditionalOnProperty(prefix = "app.search", name = "enabled", havingValue = "true")
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductDocument>>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String brandId,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice,
            @RequestParam(required = false) Boolean featured,
            @ParameterObject PageParams pageParams) {

        ProductSearchRequest request = ProductSearchRequest.builder()
                .keyword(keyword)
                .categoryId(categoryId != null ? java.util.UUID.fromString(categoryId) : null)
                .brandId(brandId != null ? java.util.UUID.fromString(brandId) : null)
                .minPrice(minPrice != null ? new java.math.BigDecimal(minPrice) : null)
                .maxPrice(maxPrice != null ? new java.math.BigDecimal(maxPrice) : null)
                .featured(featured)
                .page(pageParams.getPage())
                .size(pageParams.getSize())
                .sortBy(pageParams.getSortBy())
                .sortDirection(pageParams.getSortDirection())
                .build();

        PageResponse<ProductDocument> response = productSearchService.searchProducts(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
