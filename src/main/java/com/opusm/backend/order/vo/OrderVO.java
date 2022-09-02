package com.opusm.backend.order.vo;

import com.opusm.backend.common.support.convert.ModelMappers;
import com.opusm.backend.order.Order;
import com.opusm.backend.order.PayMethod;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderVO {

    private Long id;
    private int totalPrice;
    private PayMethod paymentMethod;
    private List<OrderProductVO> orderProducts;

    public OrderVO(Order order) {
        ModelMappers.modelMapper.map(order, this);
    }
}
