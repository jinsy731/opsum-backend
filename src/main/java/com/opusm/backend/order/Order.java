package com.opusm.backend.order;

import com.opusm.backend.customer.Customer;
import com.opusm.backend.order.dto.OrderCreateDto;
import com.opusm.backend.order.dto.OrderCreateDto.OrderCreateRequest;
import com.opusm.backend.order.dto.OrderProductCreateDto;
import com.opusm.backend.order.dto.OrderProductCreateDto.OrderProductCreateRequest;
import com.opusm.backend.product.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    private PayMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public Order(Customer customer, OrderProduct orderProduct, PayMethod paymentMethod) {
        this.customer = customer;
        this.orderProducts.add(orderProduct);
        this.paymentMethod = paymentMethod;
        this.totalPrice = calculateTotalPrice(this.orderProducts);
    }
    public Order(Customer customer, List<OrderProduct> orderProducts, PayMethod paymentMethod) {
        this.customer = customer;
        orderProducts.stream().forEach(orderProduct -> this.orderProducts.add(orderProduct));
        this.paymentMethod = paymentMethod;
        this.totalPrice = calculateTotalPrice(this.orderProducts);
    }

    private int calculateTotalPrice(List<OrderProduct> orderProducts) {
        return orderProducts.stream().mapToInt(it -> it.getAmount() * it.getProduct().getPrice()).sum();
    }
}
