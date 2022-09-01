package com.opusm.backend.customer;

import com.opusm.backend.cart.Cart;
import com.opusm.backend.common.support.BaseTimeEntity;
import com.opusm.backend.common.support.convert.ConversionUtils;
import com.opusm.backend.customer.dto.CustomerUpdateDto;
import com.opusm.backend.order.Order;
import com.opusm.backend.order.OrderProduct;
import com.opusm.backend.order.PayMethod;
import com.opusm.backend.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.opusm.backend.common.exception.Preconditions.*;
import static com.opusm.backend.customer.dto.CustomerUpdateDto.*;

@Getter
@Setter
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

    public Customer(String name, int points, int assets, Cart cart) {
        require(Strings.isNotBlank(name));
        require(points >= 0);
        require(assets >= 0);

        this.name = name;
        this.points = points;
        this.assets = assets;
        this.cart = cart;
    }

    public void update(CustomerUpdateRequest request) {
        require(request.getAssets() >= 0);
        require(request.getPoints() >= 0);

        ConversionUtils.entityUpdate(request, this);
    }

    public void updateAssetsAndPointByOrder(Order order) {
        PayMethod paymentMethod = order.getPaymentMethod();
        List<OrderProduct> orderProducts = order.getOrderProducts();

        switch (paymentMethod) {
            case ASSET:
                deductAssets(order);
                orderProducts.stream().forEach(orderProduct -> earnPoints(orderProduct));
                break;
            case POINT:
                deductPoints(order);
                break;
        }
    }

    public void deductAssets(Order order) {
        this.assets -= order.getTotalPrice();
    }

    public void deductPoints(Order order) {
        this.points -= order.getTotalPrice();
    }

    public void earnPoints(OrderProduct orderProduct) {
        Product product = orderProduct.getProduct();
        this.points += product.getPrice() * product.getPointRate() * orderProduct.getAmount();
    }
}
