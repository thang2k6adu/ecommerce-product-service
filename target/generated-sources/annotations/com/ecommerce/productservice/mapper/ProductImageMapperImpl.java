package com.ecommerce.productservice.mapper;

import com.ecommerce.productservice.dto.request.CreateProductImageRequest;
import com.ecommerce.productservice.dto.response.ProductImageResponse;
import com.ecommerce.productservice.entity.ProductImage;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-12T21:35:13+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ProductImageMapperImpl implements ProductImageMapper {

    @Override
    public ProductImage toEntity(CreateProductImageRequest request) {
        if ( request == null ) {
            return null;
        }

        ProductImage.ProductImageBuilder productImage = ProductImage.builder();

        productImage.altText( request.getAltText() );
        productImage.displayOrder( request.getDisplayOrder() );
        productImage.imageUrl( request.getImageUrl() );
        productImage.isPrimary( request.getIsPrimary() );

        return productImage.build();
    }

    @Override
    public ProductImageResponse toResponse(ProductImage image) {
        if ( image == null ) {
            return null;
        }

        ProductImageResponse.ProductImageResponseBuilder productImageResponse = ProductImageResponse.builder();

        productImageResponse.altText( image.getAltText() );
        productImageResponse.createdAt( image.getCreatedAt() );
        productImageResponse.displayOrder( image.getDisplayOrder() );
        productImageResponse.id( image.getId() );
        productImageResponse.imageUrl( image.getImageUrl() );
        productImageResponse.isPrimary( image.getIsPrimary() );

        return productImageResponse.build();
    }

    @Override
    public List<ProductImageResponse> toResponseList(List<ProductImage> images) {
        if ( images == null ) {
            return null;
        }

        List<ProductImageResponse> list = new ArrayList<ProductImageResponse>( images.size() );
        for ( ProductImage productImage : images ) {
            list.add( toResponse( productImage ) );
        }

        return list;
    }
}
