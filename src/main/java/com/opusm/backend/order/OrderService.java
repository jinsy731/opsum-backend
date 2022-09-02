package com.opusm.backend.order;

import com.opusm.backend.customer.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.opusm.backend.order.dto.OrderCreateDto.*;

public interface OrderService {

    @Transactional
    Order singleOrder(OrderCreateRequest req);

    @Transactional
    Order cartOrder(OrderCreateRequest req);

}
