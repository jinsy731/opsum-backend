package com.opusm.backend.cart.vo;

import com.opusm.backend.cart.Cart;
import com.opusm.backend.common.support.convert.ModelMappers;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartVO {

    private Long id;
    private Integer totalPrice;
    private List<CartProductVO> cartProducts;

    public CartVO(Cart cart) {
        ModelMappers.modelMapper.map(cart, this);
    }

}
