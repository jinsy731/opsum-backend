package com.opusm.backend.product;

import com.opusm.backend.base.BaseServiceTest;
import com.opusm.backend.product.dto.ProductCreateDto;
import com.opusm.backend.product.dto.ProductUpdateDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.opusm.backend.product.dto.ProductCreateDto.*;
import static com.opusm.backend.product.dto.ProductUpdateDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class DomainProductServiceTest extends BaseServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void 상품_ID_검색() {
        Product product = new Product("product", 1000, 0.5f, 10, "owner");
        productRepository.save(product);

        Product findProduct = productService.findById(product.getId());
        assertThat(findProduct).isEqualTo(product);
    }

    @Test
    void 상품_생성_성공() {
        ProductCreateRequest req = new ProductCreateRequest("product", 1000, 0.1f, 10, "owner");
        Product product = productService.create(req);

        assertThat(product.getName()).isEqualTo(req.getName());
        assertThat(product.getPrice()).isEqualTo(req.getPrice());
        assertThat(product.getPointRate()).isEqualTo(req.getPointRate());
        assertThat(product.getStock()).isEqualTo(req.getStock());
        assertThat(product.getOwner()).isEqualTo(req.getOwner());
        assertThat(product.getId()).isNotNull();
    }

    @Test
    void 상품_업데이트_성공() {
        Product product = new Product("product", 1000, 0.1f, 10, "owner");
        productRepository.save(product);

        ProductUpdateRequest req = new ProductUpdateRequest("new product", 10000, 0.5f, 20, "new owner");
        productService.update(product.getId(), req);

        assertThat(product.getName()).isEqualTo("new product");
        assertThat(product.getPrice()).isEqualTo(10000);
        assertThat(product.getPointRate()).isEqualTo(0.5f);
        assertThat(product.getStock()).isEqualTo(20);
        assertThat(product.getOwner()).isEqualTo("new owner");
    }

    @Test
    void 상품_업데이트_성공_이름만_변경() {
        Product product = new Product("product", 1000, 0.1f, 10, "owner");
        productRepository.save(product);

        ProductUpdateRequest req = new ProductUpdateRequest("new product", null, null, null, null);
        productService.update(product.getId(), req);

        assertThat(product.getName()).isEqualTo("new product");
        assertThat(product.getPrice()).isEqualTo(1000);
        assertThat(product.getPointRate()).isEqualTo(0.1f);
        assertThat(product.getStock()).isEqualTo(10);
        assertThat(product.getOwner()).isEqualTo("owner");
    }

    @Test
    void 상품_업데이트_성공_가격만_변경() {
        Product product = new Product("product", 1000, 0.1f, 10, "owner");
        productRepository.save(product);

        ProductUpdateRequest req = new ProductUpdateRequest(null, 10000, null, null, null);
        productService.update(product.getId(), req);

        assertThat(product.getName()).isEqualTo("product");
        assertThat(product.getPrice()).isEqualTo(10000);
        assertThat(product.getPointRate()).isEqualTo(0.1f);
        assertThat(product.getStock()).isEqualTo(10);
        assertThat(product.getOwner()).isEqualTo("owner");
    }

    @Test
    void 상품_업데이트_성공_포인트비율만_변경() {
        Product product = new Product("product", 1000, 0.1f, 10, "owner");
        productRepository.save(product);

        ProductUpdateRequest req = new ProductUpdateRequest(null, null, 0.5f, null, null);
        productService.update(product.getId(), req);

        assertThat(product.getName()).isEqualTo("product");
        assertThat(product.getPrice()).isEqualTo(1000);
        assertThat(product.getPointRate()).isEqualTo(0.5f);
        assertThat(product.getStock()).isEqualTo(10);
        assertThat(product.getOwner()).isEqualTo("owner");
    }

    @Test
    void 상품_업데이트_성공_재고만_변경() {
        Product product = new Product("product", 1000, 0.1f, 10, "owner");
        productRepository.save(product);

        ProductUpdateRequest req = new ProductUpdateRequest(null, null, null, 5, null);
        productService.update(product.getId(), req);

        assertThat(product.getName()).isEqualTo("product");
        assertThat(product.getPrice()).isEqualTo(1000);
        assertThat(product.getPointRate()).isEqualTo(0.1f);
        assertThat(product.getStock()).isEqualTo(5);
        assertThat(product.getOwner()).isEqualTo("owner");
    }

    @Test
    void 상품_업데이트_성공_소유자만_변경() {
        Product product = new Product("product", 1000, 0.1f, 10, "owner");
        productRepository.save(product);

        ProductUpdateRequest req = new ProductUpdateRequest(null, null, null, null, "new owner");
        productService.update(product.getId(), req);

        assertThat(product.getName()).isEqualTo("product");
        assertThat(product.getPrice()).isEqualTo(1000);
        assertThat(product.getPointRate()).isEqualTo(0.1f);
        assertThat(product.getStock()).isEqualTo(10);
        assertThat(product.getOwner()).isEqualTo("new owner");
    }

    @ParameterizedTest
    @EmptySource
    void 상품_업데이트_실패_이름이_빈값일_경우(String name) {
        Product product = new Product("product", 1000, 0.1f, 10, "owner");
        productRepository.save(product);

        ProductUpdateRequest req = new ProductUpdateRequest(name, null, null, null, null);

        assertThatIllegalArgumentException().isThrownBy(() -> productService.update(product.getId(), req));
    }

    @ParameterizedTest
    @EmptySource
    void 상품_업데이트_실패_소유자가_빈값일_경우(String owner) {
        Product product = new Product("product", 1000, 0.1f, 10, "owner");
        productRepository.save(product);

        ProductUpdateRequest req = new ProductUpdateRequest(null, null, null, null, owner);

        assertThatIllegalArgumentException().isThrownBy(() -> productService.update(product.getId(), req));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void 상품_업데이트_실패_가격이_0이하일_경우(int price) {
        Product product = new Product("product", 1000, 0.1f, 10, "owner");
        productRepository.save(product);

        ProductUpdateRequest req = new ProductUpdateRequest(null, price, null, null, null);

        assertThatIllegalArgumentException().isThrownBy(() -> productService.update(product.getId(), req));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10})
    void 상품_업데이트_실패_재고가_음수일_경우(int stock) {
        Product product = new Product("product", 1000, 0.1f, 10, "owner");
        productRepository.save(product);

        ProductUpdateRequest req = new ProductUpdateRequest(null, null, null, stock, null);

        assertThatIllegalArgumentException().isThrownBy(() -> productService.update(product.getId(), req));
    }

    @ParameterizedTest
    @ValueSource(floats = {-0.1f, -1f, -10f})
    void 상품_업데이트_실패_포인트비율이_음수일_경우(float pointRate) {
        Product product = new Product("product", 1000, 0.1f, 10, "owner");
        productRepository.save(product);

        ProductUpdateRequest req = new ProductUpdateRequest(null, null, pointRate, null, null);

        assertThatIllegalArgumentException().isThrownBy(() -> productService.update(product.getId(), req));
    }

    @Test
    void 상품_삭제() {
        Product product = new Product("product", 1000, 0.1f, 20, "owner");
        productRepository.save(product);

        Product findProduct = productRepository.findById(product.getId()).orElseThrow();

        assertThat(findProduct).isEqualTo(product);
    }

}