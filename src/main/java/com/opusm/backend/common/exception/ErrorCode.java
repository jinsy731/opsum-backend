package com.opusm.backend.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    TEST("test.error.message"),
    PRODUCT_NOT_ENOUGH_STOCK("error.product.stock.short"),
    CUSTOMER_NOT_ENOUGH_ASSETS("error.customer.assets.short"),
    CUSTOMER_NOT_ENOUGH_POINTS("error.customer.points.short");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
