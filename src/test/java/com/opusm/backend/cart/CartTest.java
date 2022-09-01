package com.opusm.backend.cart;

import com.opusm.backend.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.persistence.EntityNotFoundException;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

class CartTest {

    @Test
    void 카트_생성_성공() {
        Product product = new Product("product1", 2000, 0.5f, 5,"owner1");
        Cart cart = new Cart(3, product);

        assertThat(cart.getTotalPrice()).isEqualTo(2000 * 3);
        assertThat(cart.getCartProducts().size()).isEqualTo(1);
        assertThat(cart.getCartProducts().stream().findFirst().get().getAmount()).isEqualTo(3);
        assertThat(cart.getCartProducts().stream().findFirst().get().getProduct()).isEqualTo(product);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10, -100})
    void 카트_생성_실패_수량이_0이하인_경우(int amount) {
        Product product = new Product("product1", 2000, 0.5f, 5,"owner1");

        assertThatIllegalArgumentException().isThrownBy(() -> new Cart(amount, product));
    }

    @Test
    void 카트_생성_실패_상품이_null일_경우() {

        assertThatNullPointerException().isThrownBy(() -> new Cart(3, null));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10, -100})
    void 카트_추가_실패_수량이_0이하인_경우(int amount) {
        Product product1 = new Product("product1", 2000, 0.5f, 5,"owner1");
        Product product2 = new Product("product2", 4000, 0.5f, 5,"owner2");

        Cart cart = new Cart(3, product1);

        assertThatIllegalArgumentException().isThrownBy(() -> cart.addProduct(amount, product2));
    }

    @Test
    void 카트_추가_성공() {
        Product product1 = new Product("product1", 2000, 0.5f, 5, "owner1");
        Product product2 = new Product("product2", 4000, 0.5f, 5,"owner2");

        Cart cart = new Cart(3, product1);
        int currentTotalPrice = cart.getTotalPrice();
        cart.addProduct(4, product2);

        assertThat(cart.getCartProducts().size()).isEqualTo(2);
        assertThat(cart.getTotalPrice()).isEqualTo(currentTotalPrice + product2.getPrice() * 4);
    }

    @Test
    void 카트_추가_실패_상품이_null일_경우() {
        Product product1 = new Product("product1", 2000, 0.5f, 5, "owner1");
        Cart cart = new Cart(3, product1);

        assertThatNullPointerException().isThrownBy(() -> cart.addProduct(3, null));
    }

    @Test
    void 카트_삭제_성공() {
        Product product1 = new Product("product1", 2000, 0.5f, 5, "owner1");
        Product product2 = new Product("product2", 4000, 0.5f, 5,"owner2");
        Cart cart = new Cart(10, product1);
        cart.addProduct(10, product2);

        cart.deleteProduct("product1");

        assertThat(cart.getCartProducts().size()).isEqualTo(1);
        assertThat(cart.getTotalPrice()).isEqualTo(40000);
    }

    @Test
    void 카트_삭제_실패_해당하는_상품_이름이_없을_때() {
        Product product1 = new Product("product1", 2000, 0.5f, 5, "owner1");
        Product product2 = new Product("product2", 4000, 0.5f, 5,"owner2");
        Cart cart = new Cart(10, product1);
        cart.addProduct(10, product2);

        assertThatThrownBy(() -> cart.deleteProduct("notValidName")).isInstanceOf(NoSuchElementException.class);
    }

}