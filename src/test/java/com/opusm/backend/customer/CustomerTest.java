package com.opusm.backend.customer;

import com.opusm.backend.customer.dto.CustomerUpdateDto;
import com.opusm.backend.order.Order;
import com.opusm.backend.order.OrderProduct;
import com.opusm.backend.order.PayMethod;
import com.opusm.backend.order.dto.OrderCreateDto;
import com.opusm.backend.product.Product;
import org.assertj.core.api.Assertions;
import org.hibernate.annotations.Parameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

import static com.opusm.backend.customer.dto.CustomerUpdateDto.*;
import static com.opusm.backend.order.dto.OrderCreateDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void 유저_생성_성공() {
        Customer customer = new Customer("customer1", 2000, 20);

        assertThat(customer.getName()).isEqualTo("customer1");
        assertThat(customer.getAssets()).isEqualTo(20);
        assertThat(customer.getPoints()).isEqualTo(2000);
    }

    @Test
    void 유저_기본_자산_포인트_테스트() {
        Customer customer = new Customer("customer1");

        assertThat(customer.getAssets()).isEqualTo(0);
        assertThat(customer.getPoints()).isEqualTo(0);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 유저_생성_실패_이름이_null_또는_빈값일_경우(String name) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Customer(name));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100})
    void 유저_생성_실패_음수_자산일_경우(int assets) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Customer("customer1", 2000, assets));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100})
    void 유저_생성_실패_음수_포인트일_경우(int points) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Customer("customer1", points, 1000));
    }

    @Test
    void 유저_업데이트_성공() {
        Customer customer = new Customer("customer1", 1000, 10000);
        CustomerUpdateRequest req = new CustomerUpdateRequest("new customer name", 2000, 1000);

        customer.update(req);

        assertThat(customer.getName()).isEqualTo("new customer name");
        assertThat(customer.getAssets()).isEqualTo(2000);
        assertThat(customer.getPoints()).isEqualTo(1000);
    }

    @Test
    void 유저_업데이트_성공_이름이_null일_경우() {
        Customer customer = new Customer("customer1", 1000, 10000);
        CustomerUpdateRequest req = new CustomerUpdateRequest(null, 2000, 1000);

        customer.update(req);

        assertThat(customer.getName()).isEqualTo("customer1");
        assertThat(customer.getAssets()).isEqualTo(2000);
        assertThat(customer.getPoints()).isEqualTo(1000);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100})
    void 유저_업데이트_실패_포인트가_음수일_경우(int points) {
        Customer customer = new Customer("customer1", 1000, 10000);
        CustomerUpdateRequest req = new CustomerUpdateRequest("new customer", 2000, points);

        assertThatIllegalArgumentException().isThrownBy(() -> customer.update(req));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100})
    void 유저_업데이트_실패_자산이_음수일_경우(int assets) {
        Customer customer = new Customer("customer1", 1000, 10000);
        CustomerUpdateRequest req = new CustomerUpdateRequest("new customer", assets, 1000);

        assertThatIllegalArgumentException().isThrownBy(() -> customer.update(req));
    }

    @Test
    void 유저_주문_자산_포인트_업데이트_자산으로_결제() {
        Customer customer = new Customer("name1", 100000, 100000);
        Product product1 = new Product("product1", 1000, 0.1f, 10, "owner1");
        Product product2 = new Product("product2", 500, 0.5f, 10, "owner");
        OrderProduct orderProduct1 = new OrderProduct(10, product1);
        OrderProduct orderProduct2 = new OrderProduct(10, product2);

        Order order = new Order(customer, Arrays.asList(orderProduct1, orderProduct2), PayMethod.ASSET);
        customer.updateAssetsAndPointByOrder(order);

        assertThat(customer.getAssets()).isEqualTo(85000);
        assertThat(customer.getPoints()).isEqualTo(103500);
    }
}