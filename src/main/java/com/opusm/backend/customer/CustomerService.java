package com.opusm.backend.customer;

import com.opusm.backend.cart.Cart;
import com.opusm.backend.customer.dto.CustomerCreateDto;
import com.opusm.backend.customer.dto.CustomerUpdateDto;
import com.opusm.backend.order.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.opusm.backend.customer.dto.CustomerUpdateDto.*;

public interface CustomerService {
    @Transactional
    Customer create(Customer customer);

    Customer findById(Long customerId);

    List<Customer> findAllWithPage(Pageable pageable);

    Cart findCartByCustomerId(Long customerId);

    List<Order> findCustomerOrders(Long customerId, Pageable pageable);

    @Transactional
    Customer update(Long customerId, CustomerUpdateRequest req);
}
