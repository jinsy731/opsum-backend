package com.opusm.backend.product;

import com.opusm.backend.cart.CartProduct;
import com.opusm.backend.common.exception.ErrorMessageConst;
import com.opusm.backend.common.exception.Preconditions;
import com.opusm.backend.common.support.BaseTimeEntity;
import com.opusm.backend.order.Order;
import com.opusm.backend.order.OrderProduct;
import com.opusm.backend.product.dto.ProductUpdateDto;
import lombok.*;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.opusm.backend.common.exception.ErrorMessageConst.*;
import static com.opusm.backend.common.exception.Preconditions.*;
import static com.opusm.backend.product.dto.ProductUpdateDto.*;

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

    public Product(String name, int price, float pointRate, int stock, String owner) {
        require(Strings.isNotBlank(name), PRODUCT_NAME_BLANK);
        require(Strings.isNotBlank(owner), PRODUCT_OWNER_BLANK);
        require(price > 0, PRODUCT_PRICE_NEGATIVE);
        require(pointRate > 0f, PRODUCT_POINT_RATE_NEGATIVE);
        require(pointRate <= 1f, PRODUCT_POINT_RATE_LIMIT);
        require(stock > 0, PRODUCT_STOCK_NEGATIVE);

        this.name = name;
        this.price = price;
        this.pointRate = pointRate;
        this.owner = owner;
        this.stock = stock;
    }
    public void deductStock(int amount) {
        this.stock -= amount;
    }

    public void update(ProductUpdateRequest req) {
        require(req.getName() == null || Strings.isNotEmpty(req.getName()), PRODUCT_NAME_BLANK);
        require(req.getOwner() == null || Strings.isNotEmpty(req.getOwner()), PRODUCT_OWNER_BLANK);
        require(req.getPrice() == null || req.getPrice() > 0, PRODUCT_PRICE_NEGATIVE);
        require(req.getPointRate() == null || req.getPointRate() >= 0f, PRODUCT_POINT_RATE_NEGATIVE);
        require(req.getPointRate() == null || req.getPointRate() <= 1f, PRODUCT_POINT_RATE_LIMIT);
        require(req.getStock() == null || req.getStock() >= 0, PRODUCT_STOCK_NEGATIVE);

        this.name = (req.getName() != null) ? req.getName() : this.name;
        this.owner = (req.getOwner() != null) ? req.getOwner() : this.owner;
        this.price = (req.getPrice() != null) ? req.getPrice() : this.price;
        this.pointRate = (req.getPointRate() != null) ? req.getPointRate() : this.pointRate;
        this.stock = (req.getStock() != null) ? req.getStock() : this.stock;
    }
}
