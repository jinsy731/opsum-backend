package com.opusm.backend.cart;

import com.opusm.backend.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

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
    private List<CartProduct> cartProducts = new ArrayList<>();

    public Cart(int amount, Product product) {
        require(amount >= 1);
        notNull(product);
        
        this.cartProducts = new ArrayList<>(Arrays.asList(new CartProduct(amount, product, this)));
        this.totalPrice = amount * product.getPrice();
    }
    
    public void addProduct(int amount, Product product) {
        require(amount >= 1);
        notNull(product);

        this.cartProducts.add(new CartProduct(amount, product, this));
        this.totalPrice += (amount * product.getPrice());
    }

    public void deleteProduct(String productName) {
        CartProduct findCartProduct = findCartProduct(productName);

        this.totalPrice -= findCartProduct.getAmount() * findCartProduct.getProduct().getPrice();
        this.cartProducts.remove(findCartProduct);
    }

    private CartProduct findCartProduct(String productName) {
        return this.cartProducts.stream().filter(cartProduct -> cartProduct.getProduct().getName()
                        .equals(productName))
                .findAny()
                .orElseThrow();
    }

    public void clearCart() {
        this.cartProducts = new ArrayList<>();
        this.totalPrice = 0;
    }
}
