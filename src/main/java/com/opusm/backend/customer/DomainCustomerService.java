package com.opusm.backend.customer;

import com.opusm.backend.cart.Cart;
import com.opusm.backend.order.Order;
import com.opusm.backend.order.OrderRepository;
import com.opusm.backend.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DomainCustomerService implements CustomerService{

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Override
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer findById(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow();
    }

    public List<Customer> findAllWithPage(Pageable pageable) {
        return customerRepository.findAll(pageable).getContent();
    }

    @Override
    public Cart findCartByCustomerId(Long customerId) {
        Customer customer = findById(customerId);

        return customer.getCart();
    }

    @Override
    public List<Order> findCustomerOrders(Long customerId, Pageable pageable) {
        return orderRepository.findAllByCustomerId(customerId, pageable).getContent();
    }
}
