package com.ecommerce.productservice.common.pagination;

import com.ecommerce.productservice.common.exception.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

public final class PageableFactory {

    private PageableFactory() {
    }

    public static Pageable unsorted(int page, int size) {
        return PageRequest.of(normalizePage(page), normalizeSize(size));
    }

    public static Pageable sorted(int page, int size, String sortBy, String sortDirection, Set<String> allowedFields) {
        String field = resolveSortField(sortBy, allowedFields);
        Sort sort = isDescending(sortDirection)
                ? Sort.by(field).descending()
                : Sort.by(field).ascending();
        return PageRequest.of(normalizePage(page), normalizeSize(size), sort);
    }

    public static Pageable sorted(int page, int size, Sort sort) {
        return PageRequest.of(normalizePage(page), normalizeSize(size), sort);
    }

    private static String resolveSortField(String sortBy, Set<String> allowedFields) {
        if (sortBy == null || sortBy.isBlank()) {
            throw new BadRequestException("sortBy must not be blank");
        }
        if (!allowedFields.contains(sortBy)) {
            throw new BadRequestException("Invalid sort field: " + sortBy);
        }
        return sortBy;
    }

    private static boolean isDescending(String sortDirection) {
        return sortDirection != null && sortDirection.equalsIgnoreCase("desc");
    }

    private static int normalizePage(int page) {
        return Math.max(page, PaginationConstants.DEFAULT_PAGE);
    }

    private static int normalizeSize(int size) {
        if (size < 1) {
            return PaginationConstants.DEFAULT_SIZE;
        }
        return Math.min(size, PaginationConstants.MAX_SIZE);
    }
}
