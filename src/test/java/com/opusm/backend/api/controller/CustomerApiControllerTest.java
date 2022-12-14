package com.opusm.backend.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opusm.backend.base.BaseApiTest;
import com.opusm.backend.cart.Cart;
import com.opusm.backend.customer.Customer;
import com.opusm.backend.customer.CustomerService;
import com.opusm.backend.customer.dto.CustomerUpdateDto;
import com.opusm.backend.customer.dto.CustomerUpdateDto.CustomerUpdateRequest;
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
    void ??????_??????_??????() throws Exception {

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
                                fieldWithPath("name").description("?????? ??????"),
                                fieldWithPath("assets").description("??????"),
                                fieldWithPath("points").description("?????????"))
                        , responseFields(
                                fieldWithPath("id").description("?????? ID"),
                                fieldWithPath("name").description("?????? ??????"),
                                fieldWithPath("assets").description("?????? ??????"),
                                fieldWithPath("points").description("?????? ?????????"),
                                fieldWithPath("createdDate").description("?????? ??????")
                        )
                ));

    }


    @Test
    void ??????_??????_??????() throws Exception {

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
                                parameterWithName("customerId").description("?????? ID")
                        )
                        , responseFields(
                                fieldWithPath("id").description("?????? ID"),
                                fieldWithPath("name").description("?????? ??????"),
                                fieldWithPath("assets").description("?????? ??????"),
                                fieldWithPath("points").description("?????? ?????????"),
                                fieldWithPath("createdDate").description("?????? ??????"),
                                fieldWithPath("lastModifiedDate").description("????????? ?????? ??????")
                        )));
    }

    @Test
    void ??????_?????????_??????_?????????() throws Exception {

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
                                parameterWithName("page").description("????????? ??????"),
                                parameterWithName("size").description("????????? ??? ????????? ??????"),
                                parameterWithName("sort").description("?????? ??????")
                        )
                        , responseFields(
                                fieldWithPath("[].id").description("?????? ID"),
                                fieldWithPath("[].name").description("?????? ??????"),
                                fieldWithPath("[].assets").description("?????? ??????"),
                                fieldWithPath("[].points").description("?????? ?????????"),
                                fieldWithPath("[].createdDate").description("?????? ??????"),
                                fieldWithPath("[].lastModifiedDate").description("????????? ?????? ??????")
                        )));
    }

    @Test
    void ??????_ID???_????????????_??????() throws Exception {

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
                                , pathParameters(parameterWithName("customerId").description("?????? ID"))
                                , responseFields(
                                        fieldWithPath("id").description("???????????? ID"),
                                        fieldWithPath("totalPrice").description("?????? ?????? ??? ??????"),
                                        subsectionWithPath("cartProducts").description("?????? ?????? ??????"),
                                        fieldWithPath("cartProducts.[].id").description("???????????? ????????? ID"),
                                        fieldWithPath("cartProducts.[].amount").description("?????? ??????"),
                                        fieldWithPath("cartProducts.[].product.id").description("?????? ID"),
                                        fieldWithPath("cartProducts.[].product.name").description("?????????"),
                                        fieldWithPath("cartProducts.[].product.price").description("?????? ??????"),
                                        fieldWithPath("cartProducts.[].product.pointRate").description("????????? ?????? ??????"),
                                        fieldWithPath("cartProducts.[].product.stock").description("?????? ??????"),
                                        fieldWithPath("cartProducts.[].product.owner").description("?????????")
                                )
                        )
                );

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void ??????_??????_??????(int size) throws Exception {
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
                                parameterWithName("page").description("????????? ??????"),
                                parameterWithName("size").description("????????? ??? ????????? ??????"),
                                parameterWithName("sort").description("?????? ??????")
                        )
                        , pathParameters(parameterWithName("customerId").description("?????? ID"))
                        , responseFields(
                                fieldWithPath("[].id").description("?????? ID"),
                                fieldWithPath("[].totalPrice").description("??? ?????? ??????"),
                                fieldWithPath("[].paymentMethod").description("?????? ??????"),
                                subsectionWithPath("[].orderProducts").description("?????? ?????? ??????"),
                                fieldWithPath("[].orderProducts.[].amount").description("?????? ??????"),
                                fieldWithPath("[].orderProducts.[].product.id").description("?????? ID"),
                                fieldWithPath("[].orderProducts.[].product.name").description("?????????"),
                                fieldWithPath("[].orderProducts.[].product.price").description("?????? ??????"),
                                fieldWithPath("[].orderProducts.[].product.pointRate").description("????????? ?????? ??????"),
                                fieldWithPath("[].orderProducts.[].product.stock").description("?????? ??????"),
                                fieldWithPath("[].orderProducts.[].product.owner").description("?????????")
                        )));
    }

    @Test
    void ??????_??????_????????????() throws Exception {
        Customer customer = new Customer("customer", 1000, 1000);
        customerService.create(customer);
        CustomerUpdateRequest req = new CustomerUpdateRequest("new name", 100000, 100000);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/customer/{customerId}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(customer.getId()),
                        jsonPath("$.name").value(customer.getName()),
                        jsonPath("$.assets").value(customer.getAssets()),
                        jsonPath("$.points").value(customer.getPoints()),
                        jsonPath("$.createdDate").isNotEmpty(),
                        jsonPath("$.lastModifiedDate").isNotEmpty()
                )
                .andDo(document("customer/update"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , pathParameters(parameterWithName("customerId").description("?????? ID"))
                        , requestFields(
                                fieldWithPath("name").description("?????? ??????"),
                                fieldWithPath("assets").description("??????"),
                                fieldWithPath("points").description("?????????"))
                        , responseFields(
                                fieldWithPath("id").description("?????? ID"),
                                fieldWithPath("name").description("?????? ??????"),
                                fieldWithPath("assets").description("?????? ??????"),
                                fieldWithPath("points").description("?????? ?????????"),
                                fieldWithPath("createdDate").description("?????? ??????"),
                                fieldWithPath("lastModifiedDate").description("????????? ?????? ??????")
                        )
                ));
    }

}