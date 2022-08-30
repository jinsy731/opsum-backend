package com.opusm.backend.cart.dto;

import com.opusm.backend.product.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartProductCreateDto {

    private int amount;
    private Product product;

    public CartProductCreateDto(int amount, Product product) {
        this.amount = amount;
        this.product = product;
    }
}
