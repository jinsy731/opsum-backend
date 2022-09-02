package com.opusm.backend.product;

import org.assertj.core.api.Assertions;
import org.hibernate.annotations.Parameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.repository.query.Param;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {


    @Test
    void 상품_생성_테스트() {
        Product product = new Product("product1", 2000, 0.5f, 5, "owner1");

        assertThat(product.getName()).isEqualTo("product1");
        assertThat(product.getPrice()).isEqualTo(2000);
        assertThat(product.getPointRate()).isEqualTo(0.5f);
        assertThat(product.getOwner()).isEqualTo("owner1");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 상품_생성_실패_이름이_null이거나_빈값(String name) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Product(name, 2000, 0.5f, 5, "owner"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 상품_생성_실패_소유자_이름이_null이거나_빈값(String owner) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Product("product1", 1, 0.1f, 5, owner));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10, -100})
    void 상품_생성_실패_가격이_0이하일_경우(int price) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Product("product", price, 0.1f, 5, "owner"));
    }

    @ParameterizedTest
    @ValueSource(floats = {-0.1f, -1f, -100f})
    void 상품_생성_실패_포인트비율이_음수일_경우(float pointRate) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Product("product", 10, pointRate, 5, "owner"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10, -100})
    void 상품_생성_실패_재고가_0이하일_경우(int stock) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Product("product", 10, 0.1f, stock, "owner"));
    }
}