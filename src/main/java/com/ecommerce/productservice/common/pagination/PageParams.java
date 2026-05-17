package com.ecommerce.productservice.common.pagination;

import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@ParameterObject
public class PageParams {

    private int page = PaginationConstants.DEFAULT_PAGE;
    private int size = PaginationConstants.DEFAULT_SIZE;
    private String sortBy = "createdAt";
    private String sortDirection = "desc";

    public PageParams page(int page) {
        this.page = page;
        return this;
    }

    public PageParams size(int size) {
        this.size = size;
        return this;
    }

    public PageParams sortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    public PageParams sortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
        return this;
    }
}
