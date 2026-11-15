package org.vegetabs.iamservice.common.pagination;

import org.vegetabs.iamservice.common.error.InvalidRequestException;

public record PageQuery(int page, int size) {

    public static final int MAX_SIZE = 100;

    public PageQuery {
        if (page < 0) {
            throw new InvalidRequestException("page must be >= 0");
        }
        if (size < 1 || size > MAX_SIZE) {
            throw new InvalidRequestException("size must be between 1 and " + MAX_SIZE);
        }
    }
}