package com.opusm.backend.api.controller;

import com.opusm.backend.cart.Cart;
import com.opusm.backend.cart.vo.CartVO;
import com.opusm.backend.common.support.convert.ConversionUtils;
import com.opusm.backend.customer.Customer;
import com.opusm.backend.customer.CustomerService;
import com.opusm.backend.customer.dto.CustomerUpdateDto;
import com.opusm.backend.customer.vo.CustomerVO;
import com.opusm.backend.order.Order;
import com.opusm.backend.order.OrderService;
import com.opusm.backend.order.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.opusm.backend.customer.dto.CustomerCreateDto.*;
import static com.opusm.backend.customer.dto.CustomerUpdateDto.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
@Slf4j
public class CustomerApiController {

    private final CustomerService customerService;
    private final OrderService orderService;

    @GetMapping("/{customerId}")
    public CustomerVO customer(@PathVariable Long customerId) {
        Customer customer = customerService.findById(customerId);

        return new CustomerVO(customer);
    }

    @GetMapping
    public List<CustomerVO> customers(Pageable pageable) {
        List<Customer> customers = customerService.findAllWithPage(pageable);

        return customers.stream().map(CustomerVO::new).collect(Collectors.toList());
    }

    @PostMapping
    public CustomerCreateResponse create(@RequestBody CustomerCreateRequest req) {
        Customer customer = req.toEntity();
        customerService.create(customer);

        return ConversionUtils.entityToDto(CustomerCreateResponse.class, customer);
    }

    @PatchMapping("/{customerId}")
    public CustomerUpdateResponse update(@PathVariable Long customerId, @RequestBody CustomerUpdateRequest req) {
        Customer customer = customerService.update(customerId, req);

        return new CustomerUpdateResponse(customer);
    }

    @GetMapping("/{customerId}/cart")
    public CartVO customersCart(@PathVariable Long customerId) {
        Cart cart = customerService.findCartByCustomerId(customerId);

        return new CartVO(cart);
    }

    @GetMapping("/{customerId}/order")
    public List<OrderVO> customerOrders(@PathVariable Long customerId, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        List<Order> orders = customerService.findCustomerOrders(customerId, pageable);

        return orders.stream().map(order -> new OrderVO(order)).collect(Collectors.toList());
    }
}
