package com.ecommerce.productservice.modules.productimage;

import com.ecommerce.productservice.modules.productimage.dto.CreateProductImageRequest;
import com.ecommerce.productservice.modules.productimage.dto.ProductImageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductImageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductImage toEntity(CreateProductImageRequest request);

    ProductImageResponse toResponse(ProductImage image);

    List<ProductImageResponse> toResponseList(List<ProductImage> images);
}
