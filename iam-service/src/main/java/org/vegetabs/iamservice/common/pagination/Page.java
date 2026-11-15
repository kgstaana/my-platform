package org.vegetabs.iamservice.common.pagination;

import java.util.List;
import java.util.function.Function;

public record Page<T>(List<T> items, int page, int size, long totalItems) {

    public int totalPages() {
        return size == 0 ? 0 : (int) Math.ceil((double) totalItems / size);
    }

    public <R> Page<R> map(Function<T, R> mapper) {
        return new Page<>(items.stream().map(mapper).toList(), page, size, totalItems);
    }
}