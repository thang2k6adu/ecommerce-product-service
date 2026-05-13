package com.ecommerce.productservice.mapper;

import com.ecommerce.productservice.dto.request.CreateProductVariantRequest;
import com.ecommerce.productservice.dto.response.ProductVariantResponse;
import com.ecommerce.productservice.entity.ProductVariant;
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
public class ProductVariantMapperImpl implements ProductVariantMapper {

    @Override
    public ProductVariant toEntity(CreateProductVariantRequest request) {
        if ( request == null ) {
            return null;
        }

        ProductVariant.ProductVariantBuilder productVariant = ProductVariant.builder();

        productVariant.available( request.getAvailable() );
        productVariant.barcode( request.getBarcode() );
        productVariant.color( request.getColor() );
        productVariant.imageUrl( request.getImageUrl() );
        productVariant.material( request.getMaterial() );
        productVariant.priceAdjustment( request.getPriceAdjustment() );
        productVariant.size( request.getSize() );
        productVariant.sku( request.getSku() );
        productVariant.stockQuantity( request.getStockQuantity() );
        productVariant.style( request.getStyle() );
        productVariant.weight( request.getWeight() );

        return productVariant.build();
    }

    @Override
    public ProductVariantResponse toResponse(ProductVariant variant) {
        if ( variant == null ) {
            return null;
        }

        ProductVariantResponse.ProductVariantResponseBuilder productVariantResponse = ProductVariantResponse.builder();

        productVariantResponse.available( variant.getAvailable() );
        productVariantResponse.barcode( variant.getBarcode() );
        productVariantResponse.color( variant.getColor() );
        productVariantResponse.createdAt( variant.getCreatedAt() );
        productVariantResponse.id( variant.getId() );
        productVariantResponse.imageUrl( variant.getImageUrl() );
        productVariantResponse.material( variant.getMaterial() );
        productVariantResponse.priceAdjustment( variant.getPriceAdjustment() );
        productVariantResponse.size( variant.getSize() );
        productVariantResponse.sku( variant.getSku() );
        productVariantResponse.stockQuantity( variant.getStockQuantity() );
        productVariantResponse.style( variant.getStyle() );
        productVariantResponse.updatedAt( variant.getUpdatedAt() );
        productVariantResponse.weight( variant.getWeight() );

        return productVariantResponse.build();
    }

    @Override
    public List<ProductVariantResponse> toResponseList(List<ProductVariant> variants) {
        if ( variants == null ) {
            return null;
        }

        List<ProductVariantResponse> list = new ArrayList<ProductVariantResponse>( variants.size() );
        for ( ProductVariant productVariant : variants ) {
            list.add( toResponse( productVariant ) );
        }

        return list;
    }
}
