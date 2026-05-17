package com.ecommerce.productservice.common.pagination;

import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@ParameterObject
public class CategoryActivePageParams extends PageParams {

    public CategoryActivePageParams() {
        sortBy("displayOrder");
        sortDirection("asc");
    }
}
