package org.vegetabs.iamservice.common.web.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.vegetabs.iamservice.common.pagination.Page;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResult<T>(T data, Meta meta) {

    public record Meta(int page, int size, long totalItems, int totalPages) {}

    public static <T> ApiResult<T> of(T data) {
        return new ApiResult<>(data, null);
    }

    public static <T> ApiResult<List<T>> ofPage(Page<T> page) {
        return new ApiResult<>(
            page.items(),
            new Meta(page.page(), page.size(), page.totalItems(), page.totalPages())
        );
    }
}