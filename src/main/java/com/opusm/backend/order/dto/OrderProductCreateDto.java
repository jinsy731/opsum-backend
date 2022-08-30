package com.opusm.backend.order.dto;

import com.opusm.backend.product.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductCreateDto {

    private int amount;
    private Product product;

    public OrderProductCreateDto(int amount, Product product) {
        this.amount = amount;
        this.product = product;
    }
}
