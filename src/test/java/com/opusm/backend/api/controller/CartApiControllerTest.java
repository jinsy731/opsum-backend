package com.opusm.backend.api.controller;

import com.opusm.backend.cart.Cart;
import com.opusm.backend.cart.CartService;
import com.opusm.backend.common.support.test.BaseApiTest;
import com.opusm.backend.product.Product;
import com.opusm.backend.product.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static com.opusm.backend.cart.dto.CartCreateDto.*;
import static com.opusm.backend.cart.dto.CartDeleteDto.*;
import static com.opusm.backend.cart.dto.CartUpdateDto.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartApiControllerTest extends BaseApiTest {

    @MockBean
    ProductService productService;

    @MockBean
    CartService cartService;

    @Test
    void 카트_생성_성공() throws Exception {

        Product product = new Product("product10", 5000, 0.3f, 20, "owner10");
        CartCreateRequest req = new CartCreateRequest(5, 10L);
        when(productService.findById(10L)).thenReturn(product);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.totalPrice").value(25000),
                        jsonPath("$.cartProducts").isNotEmpty()
                );

    }

    @Test
    void 카트_추가_성공() throws Exception {
        Product oldProduct = new Product("old Product", 5000, 0.5f, 20, "owner");
        Product newProduct = new Product("new Product", 10000, 0.5f, 20, "owner");
        Cart cart = new Cart(10, oldProduct);
        CartUpdateRequest req = new CartUpdateRequest(5, 20L, 1L);

        when(cartService.findById(1L)).thenReturn(cart);
        when(productService.findById(20L)).thenReturn(newProduct);

        mockMvc.perform(put("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.totalPrice").value(100000),
                        jsonPath("$.cartProducts.size()").value(2)
                );
    }

    @Test
    void 카트_삭제_성공() throws Exception {
        Product oldProduct1 = new Product("old Product", 5000, 0.5f, 20, "owner");
        Product oldProduct2 = new Product("new Product", 10000, 0.5f, 20, "owner");
        Cart cart = new Cart(10, oldProduct1);
        cart.addProduct(1, oldProduct2);

        CartDeleteRequest req = new CartDeleteRequest(1L, "old Product");
        when(cartService.findById(1L)).thenReturn(cart);

        mockMvc.perform(delete("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.totalPrice").value(50000),
                        jsonPath("$.cartProducts.size()").value(1)
                );
    }

}