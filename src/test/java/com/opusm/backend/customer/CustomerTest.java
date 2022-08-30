package com.opusm.backend.customer;

import org.assertj.core.api.Assertions;
import org.hibernate.annotations.Parameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;

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
}