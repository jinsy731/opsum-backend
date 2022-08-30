package com.opusm.backend.cart.dto;

public class CartDto {

    public static class CartCreateRequest {
        public int amount;
        public Long productId;
    }
}
