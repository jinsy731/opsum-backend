package com.opusm.backend.api.controller;

import com.opusm.backend.cart.Cart;
import com.opusm.backend.cart.CartService;
import com.opusm.backend.product.Product;
import com.opusm.backend.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.opusm.backend.cart.dto.CartCreateDto.*;
import static com.opusm.backend.cart.dto.CartDeleteDto.*;
import static com.opusm.backend.cart.dto.CartUpdateDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartApiController {

    private final CartService cartService;

    @PostMapping
    public CartCreateResponse create(@RequestBody CartCreateRequest req) {
        Cart cart = cartService.create(req);

        return new CartCreateResponse(cart);
    }

    @PutMapping
    public CartUpdateResponse addProduct(@RequestBody CartUpdateRequest req) {
        Cart cart = cartService.addProduct(req);

        return new CartUpdateResponse(cart);
    }

    @DeleteMapping
    public CartDeleteResponse deleteProduct(@RequestBody CartDeleteRequest req) {
        Cart cart = cartService.deleteProduct(req);

        return new CartDeleteResponse(cart);
    }

}
