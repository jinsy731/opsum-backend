package com.opusm.backend.customer;

import com.opusm.backend.base.BaseServiceTest;
import com.opusm.backend.cart.Cart;
import com.opusm.backend.customer.dto.CustomerUpdateDto;
import com.opusm.backend.customer.dto.CustomerUpdateDto.CustomerUpdateRequest;
import com.opusm.backend.product.Product;
import com.opusm.backend.product.ProductRepository;
import com.opusm.backend.product.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class DomainCustomerServiceTest extends BaseServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 고객_생성_성공() {
        Customer customer = new Customer("customer1", 1000, 1000);
        customerService.create(customer);

        assertThat(customer.getName()).isEqualTo("customer1");
        assertThat(customer.getPoints()).isEqualTo(1000);
        assertThat(customer.getAssets()).isEqualTo(1000);
        assertThat(customer.getId()).isNotNull();
    }

    @Test
    void 고객_리스트_조회_페이징() {
        Customer customer1 = new Customer("customer1", 1000, 1000);
        Customer customer2 = new Customer("customer2", 1000, 1000);
        Customer customer3 = new Customer("customer3", 1000, 1000);
        Customer customer4 = new Customer("customer4", 1000, 1000);
        Customer customer5 = new Customer("customer5", 1000, 1000);
        Customer customer6 = new Customer("customer6", 1000, 1000);
        Customer customer7 = new Customer("customer7", 1000, 1000);

        customerService.create(customer1);
        customerService.create(customer2);
        customerService.create(customer3);
        customerService.create(customer4);
        customerService.create(customer5);
        customerService.create(customer6);
        customerService.create(customer7);


        List<Customer> customers = customerService.findAllWithPage(PageRequest.of(0, 4));

        assertThat(customers.size()).isEqualTo(4);
    }

    @Test
    void 고객_ID로_장바구니_찾기() {

        Customer customer = new Customer("customer", 1000, 1000);
        customerService.create(customer);
        Cart cart = customer.getCart();

        Product product1 = new Product("product1", 1000, 0.1f, 10, "owner");
        Product product2 = new Product("product2", 1000, 0.1f, 10, "owner");
        Product product3 = new Product("product3", 1000, 0.1f, 10, "owner");
        cart.addProduct(1, product1);
        cart.addProduct(1, product2);
        cart.addProduct(1, product3);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        Cart findCart = customerService.findCartByCustomerId(customer.getId());

        assertThat(cart).isEqualTo(findCart);
        assertThat(findCart.getTotalPrice()).isEqualTo(3000);
    }

    @Test
    void 고객_정보_업데이트() {
        Customer customer = new Customer("customer", 1000, 1000);
        customerService.create(customer);
        CustomerUpdateRequest req = new CustomerUpdateRequest("new name", 100000, 100000);

        customerService.update(customer.getId(), req);

        assertThat(customer.getName()).isEqualTo(req.getName());
        assertThat(customer.getAssets()).isEqualTo(req.getAssets());
        assertThat(customer.getPoints()).isEqualTo(req.getPoints());
    }

}