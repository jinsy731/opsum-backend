package com.opusm.backend.order.dto;

import com.opusm.backend.order.OrderProduct;
import com.opusm.backend.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class OrderProductCreateDto {

    @Getter
    @AllArgsConstructor
    public static class OrderProductCreateRequest {
        private int amount;
        private Product product;

        public OrderProduct toEntity() {
            return new OrderProduct(amount, product);
        }
    }
}
