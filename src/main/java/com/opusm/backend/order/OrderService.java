package com.opusm.backend.order;

import com.opusm.backend.order.dto.OrderCreateDto;

import static com.opusm.backend.order.dto.OrderCreateDto.*;

public interface OrderService {

    Order singleOrder(OrderCreateRequest req);

    Order cartOrder(OrderCreateRequest req);
}
