package com.opusm.backend.customer;

import com.opusm.backend.cart.Cart;
import com.opusm.backend.common.support.BaseTimeEntity;
import com.opusm.backend.order.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.opusm.backend.common.exception.Preconditions.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(nullable = false)
    private String name;
    private int points = 0;
    private int assets = 0;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public Customer(String name) {
        require(Strings.isNotBlank(name));

        this.name = name;
    }

    public Customer(String name, int points, int assets) {
        require(Strings.isNotBlank(name));
        require(points >= 0);
        require(assets >= 0);

        this.name = name;
        this.points = points;
        this.assets = assets;
    }
}
