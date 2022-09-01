package com.opusm.backend.api.controller;

import com.opusm.backend.order.Order;
import com.opusm.backend.order.OrderService;
import com.opusm.backend.order.dto.OrderCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.opusm.backend.order.dto.OrderCreateDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderApiContorller {

    private final OrderService orderService;

    @PostMapping
    public OrderCreateResponse singleOrder(@RequestBody OrderCreateRequest req) {
        Order order = orderService.singleOrder(req);

        return new OrderCreateResponse(order);
    }

    @PostMapping("/cart")
    public OrderCreateResponse cartOrder(@RequestBody OrderCreateRequest req) {
        Order order = orderService.cartOrder(req);

        return new OrderCreateResponse(order);
    }
}
