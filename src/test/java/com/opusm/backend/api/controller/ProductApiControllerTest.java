package com.opusm.backend.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.opusm.backend.base.BaseApiTest;
import com.opusm.backend.product.Product;
import com.opusm.backend.product.ProductRepository;
import com.opusm.backend.product.ProductService;
import com.opusm.backend.product.dto.ProductCreateDto;
import com.opusm.backend.product.dto.ProductCreateDto.ProductCreateRequest;
import com.opusm.backend.product.dto.ProductUpdateDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

import static com.opusm.backend.product.dto.ProductUpdateDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductApiControllerTest extends BaseApiTest {

    @SpyBean
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @Test
    void ??????_??????() throws Exception {

        ProductCreateRequest req = new ProductCreateRequest("product", 1000, 0.1f, 10, "owner");

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpectAll(
                        jsonPath("$.id").isNotEmpty(),
                        jsonPath("$.name").value("product"),
                        jsonPath("$.price").value(1000),
                        jsonPath("$.pointRate").value(0.1),
                        jsonPath("$.stock").value(10),
                        jsonPath("$.owner").value("owner")
                )
                .andDo(document("product/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("?????????"),
                                fieldWithPath("price").description("?????? ??????"),
                                fieldWithPath("pointRate").description("????????? ?????? ??????"),
                                fieldWithPath("stock").description("?????? ??????"),
                                fieldWithPath("owner").description("?????????")
                        ),
                        responseFields(
                                fieldWithPath("id").description("?????? ID"),
                                fieldWithPath("name").description("?????????"),
                                fieldWithPath("price").description("?????? ??????"),
                                fieldWithPath("pointRate").description("????????? ?????? ??????"),
                                fieldWithPath("stock").description("?????? ??????"),
                                fieldWithPath("owner").description("?????????"),
                                fieldWithPath("createdDate").description("?????? ??????")
                        )));
    }

    @Test
    void ??????_??????() throws Exception {
        Product product = new Product("product", 1000, 0.1f, 10, "owner");
        ProductUpdateRequest req = new ProductUpdateRequest("new product", 10000, 0.5f, 100, "owner");

        Mockito.when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/product/{productId}", 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(product.getId()),
                        jsonPath("$.name").value("new product"),
                        jsonPath("$.price").value(10000),
                        jsonPath("$.pointRate").value(0.5),
                        jsonPath("$.stock").value(100),
                        jsonPath("$.owner").value("owner")
//                        jsonPath("$.createdDate").isNotEmpty(),
//                        jsonPath("$.lastModifiedDate").isNotEmpty()
                )
                .andDo(document("product/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("productId").description("?????? ID")),
                        requestFields(
                                fieldWithPath("name").description("?????????"),
                                fieldWithPath("price").description("?????? ??????"),
                                fieldWithPath("pointRate").description("????????? ?????? ??????"),
                                fieldWithPath("stock").description("?????? ??????"),
                                fieldWithPath("owner").description("?????????")
                        ),
                        responseFields(
                                fieldWithPath("id").description("?????? ID"),
                                fieldWithPath("name").description("?????????"),
                                fieldWithPath("price").description("?????? ??????"),
                                fieldWithPath("pointRate").description("????????? ?????? ??????"),
                                fieldWithPath("stock").description("?????? ??????"),
                                fieldWithPath("owner").description("?????????"),
                                fieldWithPath("createdDate").description("?????? ??????"),
                                fieldWithPath("lastModifiedDate").description("????????? ?????? ??????")
                        )));
    }

    @Test
    void ??????_??????_??????() throws Exception {
        ProductCreateRequest req = new ProductCreateRequest("product1", 1000, 0.1f, 10, "owner");
        Product product = productService.create(req);


        mockMvc.perform(RestDocumentationRequestBuilders.get("/product/{productId}", product.getId()))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(product.getId()),
                        jsonPath("$.name").value(product.getName()),
                        jsonPath("$.pointRate").value(product.getPointRate()),
                        jsonPath("$.price").value(product.getPrice()),
                        jsonPath("$.owner").value(product.getOwner()),
                        jsonPath("$.createdDate").isNotEmpty(),
                        jsonPath("$.lastModifiedDate").isNotEmpty()
                )
                .andDo(document("product/getOne"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , pathParameters(parameterWithName("productId").description("?????? ID"))
                        , responseFields(
                                fieldWithPath("id").description("?????? ID"),
                                fieldWithPath("name").description("?????????"),
                                fieldWithPath("price").description("?????? ??????"),
                                fieldWithPath("pointRate").description("????????? ?????? ??????"),
                                fieldWithPath("stock").description("?????? ??????"),
                                fieldWithPath("owner").description("?????????"),
                                fieldWithPath("createdDate").description("?????? ??????"),
                                fieldWithPath("lastModifiedDate").description("????????? ?????? ??????")
                        )));
    }

    @Test
    void ??????_?????????_?????????_??????() throws Exception {
        ProductCreateRequest req1 = new ProductCreateRequest("product1", 1000, 0.1f, 10, "owner");
        ProductCreateRequest req2 = new ProductCreateRequest("product2", 1000, 0.1f, 10, "owner");
        ProductCreateRequest req3 = new ProductCreateRequest("product3", 1000, 0.1f, 10, "owner");
        ProductCreateRequest req4 = new ProductCreateRequest("product4", 1000, 0.1f, 10, "owner");
        ProductCreateRequest req5 = new ProductCreateRequest("product5", 1000, 0.1f, 10, "owner");
        ProductCreateRequest req6 = new ProductCreateRequest("product6", 1000, 0.1f, 10, "owner");

        productService.create(req1);
        productService.create(req2);
        productService.create(req3);
        productService.create(req4);
        productService.create(req5);
        productService.create(req6);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("page","0");
        map.add("size","5");
        map.add("sort", "id,ASC");
        mockMvc.perform(get("/product")
                        .queryParams(map))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.size()").value(5),
                        jsonPath("$").isArray()
                )
                .andDo(document("product/getList"
                        , preprocessRequest(prettyPrint())
                        , preprocessResponse(prettyPrint())
                        , requestParameters(
                                parameterWithName("page").description("????????? ??????"),
                                parameterWithName("size").description("????????? ??? ????????? ??????"),
                                parameterWithName("sort").description("?????? ??????")
                        )
                , responseFields(
                                fieldWithPath("[].id").description("?????? ID"),
                                fieldWithPath("[].name").description("?????????"),
                                fieldWithPath("[].price").description("?????? ??????"),
                                fieldWithPath("[].pointRate").description("????????? ?????? ??????"),
                                fieldWithPath("[].stock").description("?????? ??????"),
                                fieldWithPath("[].owner").description("?????????"),
                                fieldWithPath("[].createdDate").description("?????? ??????"),
                                fieldWithPath("[].lastModifiedDate").description("????????? ?????? ??????")
                        )));
    }

}