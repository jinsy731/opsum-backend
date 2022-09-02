package com.opusm.backend.order.vo;

import com.opusm.backend.product.vo.ProductVO;
import lombok.Data;

@Data
public class OrderProductVO {

    private Long id;
    private int amount;
    private ProductVO product;
}
