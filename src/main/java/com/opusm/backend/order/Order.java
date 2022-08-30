package com.opusm.backend.order;

import com.opusm.backend.customer.Customer;
import com.opusm.backend.order.dto.OrderProductCreateDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order" ,cascade = CascadeType.ALL)
    private Set<OrderProduct> orderProducts = new LinkedHashSet<>();

    @Builder
    public Order(Customer customer, List<OrderProductCreateDto> orderProductCreateDtos) {
        this.customer = customer;
        this.orderProducts = orderProductCreateDtos.stream().map(dto -> new OrderProduct(dto.getAmount(), this, dto.getProduct())).collect(Collectors.toSet());
        this.totalPrice = orderProducts.stream().mapToInt(orderProduct -> orderProduct.getAmount() * orderProduct.getProduct().getPrice()).sum();
    }
}
