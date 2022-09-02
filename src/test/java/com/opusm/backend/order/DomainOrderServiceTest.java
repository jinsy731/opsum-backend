package com.opusm.backend.order;

import com.opusm.backend.base.BaseServiceTest;
import com.opusm.backend.cart.Cart;
import com.opusm.backend.common.exception.OpusmException;
import com.opusm.backend.customer.Customer;
import com.opusm.backend.customer.CustomerService;
import com.opusm.backend.order.dto.OrderCreateDto;
import com.opusm.backend.product.Product;
import com.opusm.backend.product.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.opusm.backend.order.dto.OrderCreateDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class DomainOrderServiceTest extends BaseServiceTest {

    @Autowired
    private OrderService orderService;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private ProductService productService;

    @Test
    void 단일_주문_성공_자산_사용_및_포인트_적립() {
        Customer customer = new Customer("name1", 10000, 10000);
        Product product = new Product("product1", 1000, 0.5f, 10, "owner1");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, 10L, PayMethod.ASSET, 2);

        Mockito.when(customerService.findById(any())).thenReturn(customer);
        Mockito.when(productService.findById(any())).thenReturn(product);

        Order order = orderService.singleOrder(orderCreateRequest);

        assertThat(order.getTotalPrice()).isEqualTo(2000);
        assertThat(customer.getPoints()).isEqualTo(11000);
        assertThat(customer.getAssets()).isEqualTo(8000);
        assertThat(product.getStock()).isEqualTo(8);
    }

    @Test
    void 단일_주문_성공_포인트_사용() {
        Customer customer = new Customer("name1", 10000, 10000);
        Product product = new Product("product1", 1000, 0.5f, 10, "owner1");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, 10L, PayMethod.POINT, 10);

        Mockito.when(customerService.findById(any())).thenReturn(customer);
        Mockito.when(productService.findById(any())).thenReturn(product);

        Order order = orderService.singleOrder(orderCreateRequest);

        assertThat(order.getTotalPrice()).isEqualTo(10000);
        assertThat(customer.getPoints()).isEqualTo(0);
        assertThat(customer.getAssets()).isEqualTo(10000);
        assertThat(product.getStock()).isEqualTo(0);
    }

    @Test
    void 단일_주문_실패_상품_재고_부족() {
        Customer customer = new Customer("name1", 10000, 10000);
        Product product = new Product("product1", 1000, 0.5f, 1, "owner1");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, 10L, PayMethod.ASSET, 3);

        Mockito.when(customerService.findById(any())).thenReturn(customer);
        Mockito.when(productService.findById(any())).thenReturn(product);

        assertThatExceptionOfType(OpusmException.class).isThrownBy(() -> orderService.singleOrder(orderCreateRequest))
                .withMessage("error.product.stock.short")
                .withNoCause();
    }

    @Test
    void 단일_상품_주문_자산_부족() {
        Customer customer = new Customer("name1", 10000, 1000);
        Product product = new Product("product1", 1000, 0.5f, 10, "owner1");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, 10L, PayMethod.ASSET, 2);

        Mockito.when(customerService.findById(any())).thenReturn(customer);
        Mockito.when(productService.findById(any())).thenReturn(product);

        assertThatExceptionOfType(OpusmException.class).isThrownBy(() -> orderService.singleOrder(orderCreateRequest))
                .withMessage("error.customer.assets.short")
                .withNoCause();
    }

    @Test
    void 단일_상품_주문_포인트_부족() {
        Customer customer = new Customer("name1", 1000, 10000);
        Product product = new Product("product1", 1000, 0.5f, 10, "owner1");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, 10L, PayMethod.POINT, 2);

        Mockito.when(customerService.findById(any())).thenReturn(customer);
        Mockito.when(productService.findById(any())).thenReturn(product);

        assertThatExceptionOfType(OpusmException.class).isThrownBy(() -> orderService.singleOrder(orderCreateRequest))
                .withMessage("error.customer.points.short")
                .withNoCause();
    }

    @Test
    void 장바구니_상품_주문_성공_자산_사용_포인트_업데이트() {
        Product product1 = new Product("product1", 1000, 0.5f, 10, "owner1");
        Product product2 = new Product("product2", 500, 0.5f, 10, "owner1");
        Product product3 = new Product("product3", 2000, 0.5f, 10, "owner1");

        Customer customer = new Customer("name1", 0, 100000);
        Cart cart = customer.getCart();
        cart.addProduct(5, product1);
        cart.addProduct(5, product2);
        cart.addProduct(5, product3);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, null, PayMethod.ASSET, 0);

        Mockito.when(customerService.findById(any())).thenReturn(customer);
        Order order = orderService.cartOrder(orderCreateRequest);

        List<OrderProduct> orderProducts = order.getOrderProducts();

        assertThat(order.getOrderProducts().size()).isEqualTo(3);
        assertThat(customer.getAssets()).isEqualTo(82500);
        assertThat(customer.getPoints()).isEqualTo(8750);
        assertThat(customer.getCart().getCartProducts().size()).isEqualTo(0);
        assertThat(customer.getCart().getTotalPrice()).isEqualTo(0);
        assertThat(order.getOrderProducts()).filteredOn(orderProduct -> orderProduct.getProduct().getStock() == 5)
                .containsAll(orderProducts);
    }

    @Test
    void 장바구니_상품_주문_성공_포인트_사용() {
        Product product1 = new Product("product1", 1000, 0.5f, 10, "owner1");
        Product product2 = new Product("product2", 500, 0.5f, 10, "owner1");
        Product product3 = new Product("product3", 2000, 0.5f, 10, "owner1");

        Customer customer = new Customer("name1", 100000, 0);
        Cart cart = customer.getCart();
        cart.addProduct(5, product1);
        cart.addProduct(5, product2);
        cart.addProduct(5, product3);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, null, PayMethod.POINT, 0);

        Mockito.when(customerService.findById(any())).thenReturn(customer);
        Order order = orderService.cartOrder(orderCreateRequest);

        List<OrderProduct> orderProducts = order.getOrderProducts();

        assertThat(order.getOrderProducts().size()).isEqualTo(3);
        assertThat(customer.getAssets()).isEqualTo(0);
        assertThat(customer.getPoints()).isEqualTo(82500);
        assertThat(customer.getCart().getCartProducts().size()).isEqualTo(0);
        assertThat(customer.getCart().getTotalPrice()).isEqualTo(0);
        assertThat(order.getOrderProducts()).filteredOn(orderProduct -> orderProduct.getProduct().getStock() == 5)
                .containsAll(orderProducts);
    }

    @Test
    void 장바구니_상품_주문_실패_재고_부족() {
        Product product1 = new Product("product1", 1000, 0.5f, 4, "owner1");
        Product product2 = new Product("product2", 500, 0.5f, 4, "owner1");
        Product product3 = new Product("product3", 2000, 0.5f, 4, "owner1");

        Customer customer = new Customer("name1", 100000, 0);
        Cart cart = customer.getCart();
        cart.addProduct(5, product1);
        cart.addProduct(5, product2);
        cart.addProduct(5, product3);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, null, PayMethod.POINT, 0);

        Mockito.when(customerService.findById(any())).thenReturn(customer);

        assertThatExceptionOfType(OpusmException.class).isThrownBy(() -> orderService.cartOrder(orderCreateRequest))
                .withMessage("error.product.stock.short")
                .withNoCause();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 17499})
    void 장바구니_상품_주문_실패_포인트_부족(int points) {
        Product product1 = new Product("product1", 1000, 0.5f, 10, "owner1");
        Product product2 = new Product("product2", 500, 0.5f, 10, "owner1");
        Product product3 = new Product("product3", 2000, 0.5f, 10, "owner1");

        Customer customer = new Customer("name1", points, 0);
        Cart cart = customer.getCart();
        cart.addProduct(5, product1);
        cart.addProduct(5, product2);
        cart.addProduct(5, product3);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, null, PayMethod.POINT, 0);

        Mockito.when(customerService.findById(any())).thenReturn(customer);

        assertThatExceptionOfType(OpusmException.class).isThrownBy(() -> orderService.cartOrder(orderCreateRequest))
                .withMessage("error.customer.points.short")
                .withNoCause();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 17499})
    void 장바구니_상품_주문_실패_자산_부족(int assets) {
        Product product1 = new Product("product1", 1000, 0.5f, 10, "owner1");
        Product product2 = new Product("product2", 500, 0.5f, 10, "owner1");
        Product product3 = new Product("product3", 2000, 0.5f, 10, "owner1");

        Customer customer = new Customer("name1", 100000, assets);
        Cart cart = customer.getCart();
        cart.addProduct(5, product1);
        cart.addProduct(5, product2);
        cart.addProduct(5, product3);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, null, PayMethod.ASSET, 0);

        Mockito.when(customerService.findById(any())).thenReturn(customer);

        assertThatExceptionOfType(OpusmException.class).isThrownBy(() -> orderService.cartOrder(orderCreateRequest))
                .withMessage("error.customer.assets.short")
                .withNoCause();
    }

}