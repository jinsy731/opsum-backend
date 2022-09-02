package com.opusm.backend.api.controller;

import com.opusm.backend.base.BaseApiTest;
import com.opusm.backend.cart.Cart;
import com.opusm.backend.cart.CartProduct;
import com.opusm.backend.cart.CartService;
import com.opusm.backend.cart.vo.CartProductVO;
import com.opusm.backend.customer.Customer;
import com.opusm.backend.customer.CustomerRepository;
import com.opusm.backend.customer.CustomerService;
import com.opusm.backend.product.Product;
import com.opusm.backend.product.ProductRepository;
import com.opusm.backend.product.ProductService;
import org.assertj.core.internal.bytebuddy.description.method.MethodDescription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.SpringApplicationJsonEnvironmentPostProcessor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestParametersSnippet;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import javax.persistence.EntityManager;
import java.util.List;

import static com.opusm.backend.cart.dto.CartCreateDto.*;
import static com.opusm.backend.cart.dto.CartDeleteDto.*;
import static com.opusm.backend.cart.dto.CartUpdateDto.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartApiControllerTest extends BaseApiTest {

    @MockBean
    private ProductService productService;
    @MockBean
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void 카트_추가_성공() throws Exception {
        Customer customer = new Customer("customer", 10000, 10000);
        Product oldProduct = new Product("old Product", 5000, 0.5f, 20, "owner");
        Product newProduct = new Product("new Product", 10000, 0.5f, 20, "owner");
        Cart cart = customer.getCart();
        cart.addProduct(10, oldProduct);
        CartUpdateRequest req = new CartUpdateRequest(5, 20L, 1L);

        when(customerService.findById(any())).thenReturn(customer);
        when(productService.findById(20L)).thenReturn(newProduct);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.totalPrice").value(100000)
                )
                .andDo(document("cart/addProduct"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestFields(
                                fieldWithPath("customerId").description("고객 ID"),
                                fieldWithPath("productId").description("상품 ID"),
                                fieldWithPath("amount").description("상품 수량")
                        )
                        , responseFields(
                                fieldWithPath("id").description("장바구니 ID"),
                                fieldWithPath("totalPrice").description("담은 상품 총 가격"),
                                subsectionWithPath("cartProducts").description("담은 상품 목록"),
                                fieldWithPath("cartProducts.[].amount").description("상품 수량"),
                                fieldWithPath("cartProducts.[].product.id").description("상품 ID"),
                                fieldWithPath("cartProducts.[].product.name").description("상품명"),
                                fieldWithPath("cartProducts.[].product.price").description("상품 가격"),
                                fieldWithPath("cartProducts.[].product.pointRate").description("포인트 적립 비율"),
                                fieldWithPath("cartProducts.[].product.stock").description("상품 재고"),
                                fieldWithPath("cartProducts.[].product.owner").description("소유자")
                        )
                ));
    }

    @Test
    void 카트_삭제_성공() throws Exception {
        Customer customer = new Customer("customer", 10000, 10000);
        Product oldProduct1 = new Product("old Product", 5000, 0.5f, 20, "owner");
        Product oldProduct2 = new Product("new Product", 10000, 0.5f, 20, "owner");

        customerRepository.save(customer);
        productRepository.save(oldProduct1);
        productRepository.save(oldProduct2);

        Cart cart = customer.getCart();
        cart.addProduct(10, oldProduct1);
        cart.addProduct(1, oldProduct2);

        CartDeleteRequest req = new CartDeleteRequest(customer.getId(), oldProduct2.getId());
        when(customerService.findById(any())).thenReturn(customer);


        mockMvc.perform(delete("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.totalPrice").value(50000),
                        jsonPath("$.cartProducts.size()").value(1)
                )
                .andDo(document("cart/deleteProduct"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestFields(
                                fieldWithPath("customerId").description("고객 ID"),
                                fieldWithPath("productId").description("상품 ID")
                        )
                        , responseFields(
                                fieldWithPath("id").description("장바구니 ID"),
                                fieldWithPath("totalPrice").description("담은 상품 총 가격"),
                                subsectionWithPath("cartProducts").description("담은 상품 목록"),
                                fieldWithPath("cartProducts.[].id").description("장바구니 상품별 ID"),
                                fieldWithPath("cartProducts.[].amount").description("상품 수량"),
                                fieldWithPath("cartProducts.[].product.id").description("상품 ID"),
                                fieldWithPath("cartProducts.[].product.name").description("상품명"),
                                fieldWithPath("cartProducts.[].product.price").description("상품 가격"),
                                fieldWithPath("cartProducts.[].product.pointRate").description("포인트 적립 비율"),
                                fieldWithPath("cartProducts.[].product.stock").description("상품 재고"),
                                fieldWithPath("cartProducts.[].product.owner").description("소유자")
                        )
                ));
    }

}