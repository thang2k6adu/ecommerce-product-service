package com.ecommerce.productservice.mapper;

import com.ecommerce.productservice.document.ProductDocument;
import com.ecommerce.productservice.entity.Product;
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
public class ProductDocumentMapperImpl implements ProductDocumentMapper {

    @Override
    public ProductDocument toDocument(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDocument.ProductDocumentBuilder productDocument = ProductDocument.builder();

        productDocument.createdAt( product.getCreatedAt() );
        productDocument.description( product.getDescription() );
        productDocument.featured( product.getFeatured() );
        productDocument.name( product.getName() );
        productDocument.price( product.getPrice() );
        productDocument.published( product.getPublished() );
        productDocument.shortDescription( product.getShortDescription() );
        productDocument.sku( product.getSku() );
        productDocument.slug( product.getSlug() );
        productDocument.stockQuantity( product.getStockQuantity() );
        productDocument.updatedAt( product.getUpdatedAt() );

        productDocument.id( product.getId().toString() );
        productDocument.categoryId( product.getCategory() != null ? product.getCategory().getId().toString() : null );
        productDocument.categoryName( product.getCategory() != null ? product.getCategory().getName() : null );
        productDocument.categorySlug( product.getCategory() != null ? product.getCategory().getSlug() : null );
        productDocument.brandId( product.getBrand() != null ? product.getBrand().getId().toString() : null );
        productDocument.brandName( product.getBrand() != null ? product.getBrand().getName() : null );
        productDocument.brandSlug( product.getBrand() != null ? product.getBrand().getSlug() : null );
        productDocument.status( product.getStatus().name() );
        productDocument.imageUrls( mapImageUrls(product) );

        return productDocument.build();
    }

    @Override
    public List<ProductDocument> toDocumentList(List<Product> products) {
        if ( products == null ) {
            return null;
        }

        List<ProductDocument> list = new ArrayList<ProductDocument>( products.size() );
        for ( Product product : products ) {
            list.add( toDocument( product ) );
        }

        return list;
    }
}
