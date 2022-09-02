package com.opusm.backend.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opusm.backend.base.BaseApiTest;
import com.opusm.backend.cart.Cart;
import com.opusm.backend.customer.Customer;
import com.opusm.backend.customer.CustomerRepository;
import com.opusm.backend.order.OrderRepository;
import com.opusm.backend.order.PayMethod;
import com.opusm.backend.order.dto.OrderCreateDto;
import com.opusm.backend.product.Product;
import com.opusm.backend.product.ProductRepository;
import com.opusm.backend.product.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.opusm.backend.order.dto.OrderCreateDto.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderApiControllerTest extends BaseApiTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void 단일_상품_주문() throws Exception {
        Customer customer = new Customer("customer", 100000, 10000);
        Product product = new Product("product1", 1000, 0.1f, 10, "owner");
        productRepository.save(product);
        customerRepository.save(customer);

        OrderCreateRequest req = new OrderCreateRequest(customer.getId(), product.getId(), PayMethod.ASSET, 10);


        mockMvc.perform(post("/order").queryParam("type", "single")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").isNotEmpty(),
                        jsonPath("$.totalPrice").value(10000),
                        jsonPath("$.orderProducts.size()").value(1)
                )
                .andDo(document("order/singleOrder"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestFields(
                                fieldWithPath("customerId").description("주문하는 고객 ID"),
                                fieldWithPath("productId").description("주문할 상품 ID"),
                                fieldWithPath("paymentMethod").description("결제 방식"),
                                fieldWithPath("amount").description("상품 주문 수량")

                        )
                        , responseFields(
                                fieldWithPath("id").description("주문 ID"),
                                fieldWithPath("totalPrice").description("총 주문 금액"),
                                fieldWithPath("paymentMethod").description("결제 방식"),
                                subsectionWithPath("orderProducts").description("주문 상품 목록"),
                                fieldWithPath("orderProducts.[].amount").description("상품 수량"),
                                fieldWithPath("orderProducts.[].product.id").description("상품 ID"),
                                fieldWithPath("orderProducts.[].product.name").description("상품명"),
                                fieldWithPath("orderProducts.[].product.price").description("상품 가격"),
                                fieldWithPath("orderProducts.[].product.pointRate").description("포인트 적립 비율"),
                                fieldWithPath("orderProducts.[].product.stock").description("상품 재고"),
                                fieldWithPath("orderProducts.[].product.owner").description("소유자")
                        )));
    }

    @Test
    void 장바구니_상품_주문() throws Exception {
        Customer customer = new Customer("customer", 100000, 100000);
        Product product1 = new Product("product1", 1000, 0.1f, 10, "owner");
        Product product2 = new Product("product2", 2000, 0.1f, 10, "owner");
        Product product3 = new Product("product3", 3000, 0.1f, 10, "owner");
        Cart cart = customer.getCart();
        cart.addProduct(10, product1);
        cart.addProduct(10, product2);
        cart.addProduct(10, product3);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        customerRepository.save(customer);

        OrderCreateRequest req = new OrderCreateRequest(customer.getId(), null, PayMethod.ASSET, null);

        mockMvc.perform(post("/order").queryParam("type", "cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").isNotEmpty(),
                        jsonPath("$.totalPrice").value(60000),
                        jsonPath("$.orderProducts.size()").value(3)
                )
                .andDo(document("order/cartOrder"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestFields(
                                fieldWithPath("productId").description("").optional(),
                                fieldWithPath("amount").description("").optional(),
                                fieldWithPath("customerId").description("주문하는 고객 ID"),
                                fieldWithPath("paymentMethod").description("결제 방식")

                        )
                        , responseFields(
                                fieldWithPath("id").description("주문 ID"),
                                fieldWithPath("totalPrice").description("총 주문 금액"),
                                fieldWithPath("paymentMethod").description("결제 방식"),
                                subsectionWithPath("orderProducts").description("주문 상품 목록"),
                                fieldWithPath("orderProducts.[].amount").description("상품 수량"),
                                fieldWithPath("orderProducts.[].product.id").description("상품 ID"),
                                fieldWithPath("orderProducts.[].product.name").description("상품명"),
                                fieldWithPath("orderProducts.[].product.price").description("상품 가격"),
                                fieldWithPath("orderProducts.[].product.pointRate").description("포인트 적립 비율"),
                                fieldWithPath("orderProducts.[].product.stock").description("상품 재고"),
                                fieldWithPath("orderProducts.[].product.owner").description("소유자")
                        )));
    }

}