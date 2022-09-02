package com.opusm.backend.common.exception;

public interface ErrorMessageConst {
    String CUSTOMER_NAME_BLANK = "이름은 빈 값일 수 없습니다.";
    String CUSTOMER_ASSETS_NEGATIVE = "자산은 음수일 수 없습니다.";
    String CUSTOMER_POINTS_NEGATIVE = "포인트는 음수일 수 없습니다.";

    String PRODUCT_NAME_BLANK = "상품명은 빈 값일 수 없습니다.";
    String PRODUCT_PRICE_NEGATIVE = "상품 가격은 음수일 수 없습니다.";
    String PRODUCT_POINT_RATE_NEGATIVE = "포인트 적립율은 음수일 수 없습니다.";
    String PRODUCT_OWNER_BLANK = "소유자 명은 빈 값일 수 없습니다.";
    String PRODUCT_STOCK_NEGATIVE = "재고 수량은 음수일 수 없습니다";
    String PRODUCT_POINT_RATE_LIMIT = "포인트 적립율은 100%를 넘을 수 없습니다.";
    String CART_AMOUNT_INVALID = "상품 수량은 1개 이상이어야 합니다.";

}
