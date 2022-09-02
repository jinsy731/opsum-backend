package com.opusm.backend.order;

import com.opusm.backend.base.BaseRepositoryTest;
import com.opusm.backend.customer.Customer;
import com.opusm.backend.customer.CustomerRepository;
import com.opusm.backend.product.Product;
import com.opusm.backend.product.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void 고객_ID로_주문_조회(int size) {
        Customer customer = customerRepository.save(new Customer("customer1", 1000, 1000));

        Product product1 = new Product("product1", 1000, 0.1f, 10, "owner");
        Product product2 = new Product("product2", 1000, 0.1f, 10, "owner");
        Product product3 = new Product("product3", 1000, 0.1f, 10, "owner");

        productRepository.saveAll(Arrays.asList(product1, product2, product3));

        Order order1 = new Order(customer, new OrderProduct(5, product1), PayMethod.ASSET);
        Order order2 = new Order(customer, new OrderProduct(5, product2), PayMethod.ASSET);
        Order order3 = new Order(customer, new OrderProduct(5, product3), PayMethod.ASSET);

        orderRepository.saveAll(Arrays.asList(order1, order2, order3));

        List<Order> orders = orderRepository.findAllByCustomerId(customer.getId(), PageRequest.of(0, size)).getContent();

        assertThat(orders.size()).isEqualTo(size);
    }

}