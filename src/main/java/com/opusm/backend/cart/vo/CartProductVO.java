package com.opusm.backend.cart.vo;

import com.opusm.backend.product.vo.ProductVO;
import lombok.Data;

@Data
public class CartProductVO {

    private Long id;
    private int amount;
    private ProductVO product;
}
