package com.opusm.backend.order;

import com.opusm.backend.customer.Customer;
import com.opusm.backend.order.dto.OrderProductCreateDto;
import com.opusm.backend.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


class OrderTest {

    List<OrderProductCreateDto> orderProductCreateDtos = new ArrayList<>();
    Customer customer = new Customer("customer1", 10000, 200);

    @BeforeEach
    void init() {
        Product product1 = new Product("product1", 1000, 0.15f, "owner1");
        Product product2 = new Product("product2", 2000, 0.2f, "owner2");
        Product product3 = new Product("product3", 3000, 0.5f, "owner3");
        orderProductCreateDtos.add(new OrderProductCreateDto(2, product1));
        orderProductCreateDtos.add(new OrderProductCreateDto(2, product2));
        orderProductCreateDtos.add(new OrderProductCreateDto(2, product3));

    }

    @Test
    void Order_생성_테스트() {
        Order order = new Order(customer, orderProductCreateDtos);

        Assertions.assertThat(order.getTotalPrice()).isEqualTo(12000);
        Assertions.assertThat(order.getOrderProducts().size()).isEqualTo(3);
    }


}