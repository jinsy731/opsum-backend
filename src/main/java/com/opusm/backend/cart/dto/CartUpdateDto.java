package com.opusm.backend.cart.dto;

import com.opusm.backend.cart.Cart;
import com.opusm.backend.cart.vo.CartProductVO;
import com.opusm.backend.common.support.convert.ModelMappers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class CartUpdateDto {

    @Getter
    @AllArgsConstructor
    public static class CartUpdateRequest {
        private int amount;
        private Long productId;
        private Long cartId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CartUpdateResponse {
        private int totalPrice;
        private List<CartProductVO> cartProducts = new ArrayList<>();

        public CartUpdateResponse(Cart cart) {
            ModelMappers.modelMapper.map(cart, this);
            this.totalPrice = cart.getTotalPrice();
        }
    }
}
