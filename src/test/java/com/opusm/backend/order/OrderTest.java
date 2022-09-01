package com.opusm.backend.order;

import com.opusm.backend.customer.Customer;
import com.opusm.backend.order.dto.OrderCreateDto;
import com.opusm.backend.order.dto.OrderProductCreateDto;
import com.opusm.backend.product.Product;
import com.opusm.backend.product.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.opusm.backend.order.dto.OrderCreateDto.*;
import static com.opusm.backend.order.dto.OrderProductCreateDto.*;


class OrderTest {

    List<OrderProductCreateRequest> orderProductCreateRequests = new ArrayList<>();
    Customer customer = new Customer("customer1", 10000, 200);

    OrderProductCreateRequest orderProductCreateRequest1;
    OrderProductCreateRequest orderProductCreateRequest2;
    OrderProductCreateRequest orderProductCreateRequest3;

    OrderCreateRequest orderCreateRequest1;
    OrderCreateRequest orderCreateRequest2;
    OrderCreateRequest orderCreateRequest3;

    OrderProduct orderProduct1;
    OrderProduct orderProduct2;
    OrderProduct orderProduct3;

    @BeforeEach
    void init() {
        Customer customer = new Customer("customer1", 10000, 10000); // 1L
        Product product1 = new Product("product1", 1000, 0.15f, 5, "owner1"); // 10L
        Product product2 = new Product("product2", 2000, 0.2f, 5,"owner2"); // 20L
        Product product3 = new Product("product3", 3000, 0.5f, 5,"owner3"); // 30L
        orderProductCreateRequest1 = new OrderProductCreateRequest(2, product1);
        orderProductCreateRequest2 = new OrderProductCreateRequest(2, product2);
        orderProductCreateRequest3 = new OrderProductCreateRequest(2, product3);
        orderProduct1 = orderProductCreateRequest1.toEntity();
        orderProduct2 = orderProductCreateRequest2.toEntity();
        orderProduct3 = orderProductCreateRequest3.toEntity();
        orderProductCreateRequests.add(orderProductCreateRequest3);
        orderProductCreateRequests.add(orderProductCreateRequest1);
        orderProductCreateRequests.add(orderProductCreateRequest2);
        OrderCreateRequest orderCreateRequest1 = new OrderCreateRequest(1L, 10L, PayMethod.ASSET, 2);
        OrderCreateRequest orderCreateRequest2 = new OrderCreateRequest(1L, 20L, PayMethod.ASSET, 2);
        OrderCreateRequest orderCreateRequest3 = new OrderCreateRequest(1L, 30L, PayMethod.POINT, 2);

    }

    @Test
    void Order_생성_테스트() {
        Order order = new Order(customer, Arrays.asList(orderProduct1, orderProduct2, orderProduct3), PayMethod.ASSET);

        Assertions.assertThat(order.getTotalPrice()).isEqualTo(12000);
        Assertions.assertThat(order.getOrderProducts().size()).isEqualTo(3);
    }


}