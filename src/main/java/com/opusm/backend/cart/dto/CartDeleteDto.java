package com.opusm.backend.cart.dto;

import com.opusm.backend.cart.Cart;
import com.opusm.backend.cart.vo.CartProductVO;
import com.opusm.backend.common.support.convert.ModelMappers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class CartDeleteDto {

    @Getter
    @AllArgsConstructor
    public static class CartDeleteRequest {
        private Long customerId;
        private Long productId;
    }

    @Getter
    @Setter
    public static class CartDeleteResponse {

        private Long id;
        private int totalPrice;
        private List<CartProductVO> cartProducts = new ArrayList<>();

        public CartDeleteResponse(Cart cart) {
            ModelMappers.modelMapper.map(cart, this);
            this.totalPrice = cart.getTotalPrice();
        }
    }
}
