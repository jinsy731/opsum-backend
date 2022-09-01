package com.opusm.backend.cart.dto;

import com.opusm.backend.cart.Cart;
import com.opusm.backend.cart.vo.CartProductVO;
import com.opusm.backend.common.support.convert.ModelMappers;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CartDeleteDto {

    @Getter
    @AllArgsConstructor
    public static class CartDeleteRequest {
        private Long cartId;
        private String productName;
    }

    public static class CartDeleteResponse {
        int totalPrice;
        List<CartProductVO> cartProducts = new ArrayList<>();

        public CartDeleteResponse(Cart cart) {
            ModelMappers.modelMapper.map(cart, this);
            this.totalPrice = cart.getTotalPrice();
        }
    }
}
