package com.opusm.backend.product;

import com.opusm.backend.cart.CartProduct;
import com.opusm.backend.common.exception.Preconditions;
import com.opusm.backend.common.support.BaseTimeEntity;
import com.opusm.backend.order.Order;
import com.opusm.backend.order.OrderProduct;
import lombok.*;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.opusm.backend.common.exception.Preconditions.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private int stock;
    @Column(nullable = false)
    private float pointRate;
    @Column(nullable = false)
    private String owner;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @Builder
    public Product(String name, int price, float pointRate, int stock, String owner) {
        require(Strings.isNotBlank(name));
        require(Strings.isNotBlank(owner));

        this.name = name;
        this.price = price;
        this.pointRate = pointRate;
        this.owner = owner;
        this.stock = stock;
    }
    public void deductStock(int amount) {
        this.stock -= amount;
    }
}
