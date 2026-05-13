package com.ecommerce.productservice.document;

import com.ecommerce.productservice.modules.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductDocumentMapper {

    @Mapping(target = "id", expression = "java(product.getId().toString())")
    @Mapping(target = "categoryId", expression = "java(product.getCategory() != null ? product.getCategory().getId().toString() : null)")
    @Mapping(target = "categoryName", expression = "java(product.getCategory() != null ? product.getCategory().getName() : null)")
    @Mapping(target = "categorySlug", expression = "java(product.getCategory() != null ? product.getCategory().getSlug() : null)")
    @Mapping(target = "brandId", expression = "java(product.getBrand() != null ? product.getBrand().getId().toString() : null)")
    @Mapping(target = "brandName", expression = "java(product.getBrand() != null ? product.getBrand().getName() : null)")
    @Mapping(target = "brandSlug", expression = "java(product.getBrand() != null ? product.getBrand().getSlug() : null)")
    @Mapping(target = "status", expression = "java(product.getStatus().name())")
    @Mapping(target = "imageUrls", expression = "java(mapImageUrls(product))")
    ProductDocument toDocument(Product product);

    List<ProductDocument> toDocumentList(List<Product> products);

    default List<String> mapImageUrls(Product product) {
        if (product.getImages() == null) {
            return List.of();
        }
        return product.getImages().stream()
                .map(image -> image.getImageUrl())
                .collect(Collectors.toList());
    }
}
