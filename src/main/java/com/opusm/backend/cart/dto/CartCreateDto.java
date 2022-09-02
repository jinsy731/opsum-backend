package com.opusm.backend.cart.dto;

import com.opusm.backend.cart.Cart;
import com.opusm.backend.cart.vo.CartProductVO;
import com.opusm.backend.common.support.convert.ModelMappers;
import com.opusm.backend.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CartCreateDto {

    @Getter
    @AllArgsConstructor
    public static class CartCreateRequest {
        private int amount;
        private Long productId;

        public Cart toEntity(Product product) {
            return new Cart(amount, product);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CartCreateResponse {
        private int totalPrice;
        private List<CartProductVO> cartProducts = new ArrayList<>();

        public CartCreateResponse(Cart cart) {
            ModelMappers.modelMapper.map(cart, this);
            this.totalPrice = this.getCartProducts().stream().mapToInt(value -> value.getProduct().getPrice() * value.getAmount()).sum();
        }
    }
}
