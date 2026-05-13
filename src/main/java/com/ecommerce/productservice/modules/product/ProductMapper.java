package com.ecommerce.productservice.modules.product;

import com.ecommerce.productservice.modules.product.dto.CreateProductRequest;
import com.ecommerce.productservice.modules.product.dto.ProductResponse;
import com.ecommerce.productservice.modules.brand.BrandMapper;
import com.ecommerce.productservice.modules.category.CategoryMapper;
import com.ecommerce.productservice.modules.productimage.ProductImageMapper;
import com.ecommerce.productservice.modules.productvariant.ProductVariantMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CategoryMapper.class, BrandMapper.class, ProductImageMapper.class, ProductVariantMapper.class})
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "variants", ignore = true)
    Product toEntity(CreateProductRequest request);

    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "variants", ignore = true)
    void updateEntityFromRequest(CreateProductRequest request, @MappingTarget Product product);
}
