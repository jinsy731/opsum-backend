package com.opusm.backend.cart;

import com.opusm.backend.cart.dto.CartProductCreateDto;
import com.opusm.backend.common.exception.Preconditions;
import com.opusm.backend.product.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.opusm.backend.common.exception.Preconditions.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    private int totalPrice;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartProduct> cartProducts = new LinkedHashSet<>();

    public Cart(int amount, Product product) {
        require(amount >= 1);
        notNull(product);
        
        this.cartProducts = new LinkedHashSet<>(Arrays.asList(new CartProduct(amount, product, this)));
        this.totalPrice = amount * product.getPrice();
    }
    
    public void addProductToCart(int amount, Product product) {
        require(amount >= 1);
        notNull(product);

        this.cartProducts.add(new CartProduct(amount, product, this));
        this.totalPrice += (amount * product.getPrice());
    }
}
