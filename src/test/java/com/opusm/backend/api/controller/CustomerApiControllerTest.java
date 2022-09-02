package com.opusm.backend.api.controller;

import com.opusm.backend.base.BaseApiTest;
import com.opusm.backend.cart.Cart;
import com.opusm.backend.customer.Customer;
import com.opusm.backend.customer.CustomerService;
import com.opusm.backend.order.Order;
import com.opusm.backend.order.OrderProduct;
import com.opusm.backend.order.OrderService;
import com.opusm.backend.order.PayMethod;
import com.opusm.backend.order.dto.OrderCreateDto;
import com.opusm.backend.product.Product;
import com.opusm.backend.product.ProductService;
import com.opusm.backend.product.dto.ProductCreateDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

import static com.opusm.backend.customer.dto.CustomerCreateDto.*;
import static com.opusm.backend.order.dto.OrderCreateDto.*;
import static com.opusm.backend.product.dto.ProductCreateDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerApiControllerTest extends BaseApiTest {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;

    @Test
    void 고객_생성_성공() throws Exception {

        CustomerCreateRequest req = new CustomerCreateRequest("customer1", 1000, 23300);

        this.mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").isNotEmpty(),
                        jsonPath("$.name").value(req.getName()),
                        jsonPath("$.assets").value(1000),
                        jsonPath("$.points").value(23300),
                        jsonPath("$.createdDate").isNotEmpty()
                )
                .andDo(document("customer/create"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestFields(
                                fieldWithPath("name").description("고객 이름"),
                                fieldWithPath("assets").description("자산"),
                                fieldWithPath("points").description("포인트"))
                        , responseFields(
                                fieldWithPath("id").description("고객 ID"),
                                fieldWithPath("name").description("고객 이름"),
                                fieldWithPath("assets").description("고객 자산"),
                                fieldWithPath("points").description("고객 포인트"),
                                fieldWithPath("createdDate").description("생성 시각")
                        )
                ));

    }


    @Test
    void 고객_단건_조회() throws Exception {

        Customer customer = new Customer("customer", 1000, 1000);
        customerService.create(customer);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/customer/{customerId}", customer.getId()))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(customer.getId()),
                        jsonPath("$.name").value(customer.getName()),
                        jsonPath("$.assets").value(customer.getAssets()),
                        jsonPath("$.points").value(customer.getPoints()),
                        jsonPath("$.createdDate").isNotEmpty(),
                        jsonPath("$.lastModifiedDate").isNotEmpty()
                )
                .andDo(document("customer/getOne"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , pathParameters(
                                parameterWithName("customerId").description("고객 ID")
                        )
                        , responseFields(
                                fieldWithPath("id").description("고객 ID"),
                                fieldWithPath("name").description("고객 이름"),
                                fieldWithPath("assets").description("고객 자산"),
                                fieldWithPath("points").description("고객 포인트"),
                                fieldWithPath("createdDate").description("생성 시각"),
                                fieldWithPath("lastModifiedDate").description("마지막 수정 시각")
                        )));
    }

    @Test
    void 고객_리스트_조회_페이징() throws Exception {

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

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("page", "0");
        map.add("size", "5");
        map.add("sort", "id,ASC");

        mockMvc.perform(RestDocumentationRequestBuilders.get("/customer")
                        .queryParams(map))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$").isArray(),
                        jsonPath("$.size()").value(5)
                )
                .andDo(document("customer/getList"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 당 데이터 개수"),
                                parameterWithName("sort").description("정렬 방식")
                        )
                        , responseFields(
                                fieldWithPath("[].id").description("고객 ID"),
                                fieldWithPath("[].name").description("고객 이름"),
                                fieldWithPath("[].assets").description("고객 자산"),
                                fieldWithPath("[].points").description("고객 포인트"),
                                fieldWithPath("[].createdDate").description("생성 시각"),
                                fieldWithPath("[].lastModifiedDate").description("마지막 수정 시각")
                        )));
    }

    @Test
    void 고객_ID로_장바구니_조회() throws Exception {

        Customer customer = new Customer("customer", 1000, 1000);
        customerService.create(customer);
        Cart cart = customer.getCart();

        ProductCreateRequest req1 = new ProductCreateRequest("product1", 1000, 0.1f, 10, "owner");
        ProductCreateRequest req2 = new ProductCreateRequest("product2", 1000, 0.1f, 10, "owner");
        ProductCreateRequest req3 = new ProductCreateRequest("product3", 1000, 0.1f, 10, "owner");
        Product product1 = productService.create(req1);
        Product product2 = productService.create(req2);
        Product product3 = productService.create(req3);

        cart.addProduct(1, product1);
        cart.addProduct(1, product2);
        cart.addProduct(1, product3);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/customer/{customerId}/cart", customer.getId()))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(cart.getId()),
                        jsonPath("$.totalPrice").value(cart.getTotalPrice()),
                        jsonPath("$.cartProducts.size()").value(3),
                        jsonPath("$.cartProducts").isArray()
                )
                .andDo(document("customer/findCart"
                                , preprocessRequest(prettyPrint())
                                , preprocessResponse(prettyPrint())
                                , pathParameters(parameterWithName("customerId").description("고객 ID"))
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
                        )
                );

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void 고객_주문_조회(int size) throws Exception {
        Customer customer = customerService.create(new Customer("customer1", 1000, 100000));

        Product product = productService.create(new ProductCreateRequest("product1", 1000, 0.1f, 10, "owner"));
        Product product1 = productService.create(new ProductCreateRequest("product2", 1000, 0.1f, 10, "owner"));
        Product product2 = productService.create(new ProductCreateRequest("product3", 1000, 0.1f, 10, "owner"));

        orderService.singleOrder(new OrderCreateRequest(customer.getId(), product.getId(), PayMethod.ASSET, 5));
        orderService.singleOrder(new OrderCreateRequest(customer.getId(), product1.getId(), PayMethod.ASSET, 5));
        orderService.singleOrder(new OrderCreateRequest(customer.getId(), product2.getId(), PayMethod.ASSET, 5));

        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("page", "0");
        map.add("size", String.valueOf(size));
        map.add("sort", "id,ASC");

        mockMvc.perform(RestDocumentationRequestBuilders.get("/customer/{customerId}/order", customer.getId())
                        .queryParams(map))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.size()").value(size),
                        jsonPath("$").isArray()
                )
                .andDo(document("customer/getOrderList"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 당 데이터 개수"),
                                parameterWithName("sort").description("정렬 방식")
                        )
                        , pathParameters(parameterWithName("customerId").description("고객 ID"))
                        , responseFields(
                                fieldWithPath("[].id").description("주문 ID"),
                                fieldWithPath("[].totalPrice").description("총 주문 금액"),
                                fieldWithPath("[].paymentMethod").description("결제 방식"),
                                subsectionWithPath("[].orderProducts").description("주문 상품 목록"),
                                fieldWithPath("[].orderProducts.[].amount").description("상품 수량"),
                                fieldWithPath("[].orderProducts.[].product.id").description("상품 ID"),
                                fieldWithPath("[].orderProducts.[].product.name").description("상품명"),
                                fieldWithPath("[].orderProducts.[].product.price").description("상품 가격"),
                                fieldWithPath("[].orderProducts.[].product.pointRate").description("포인트 적립 비율"),
                                fieldWithPath("[].orderProducts.[].product.stock").description("상품 재고"),
                                fieldWithPath("[].orderProducts.[].product.owner").description("소유자")
                        )));
    }

}