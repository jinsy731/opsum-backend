package com.opusm.backend.product;

import org.assertj.core.api.Assertions;
import org.hibernate.annotations.Parameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
        assertThatIllegalArgumentException().isThrownBy(() -> new Product("product1", 1, 0.1f,5, owner));
    }
}