package com.opusm.backend.cart;

import com.opusm.backend.cart.dto.CartDeleteDto;
import com.opusm.backend.product.Product;
import com.opusm.backend.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.opusm.backend.cart.dto.CartCreateDto.*;
import static com.opusm.backend.cart.dto.CartDeleteDto.*;
import static com.opusm.backend.cart.dto.CartUpdateDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DomainCartService implements CartService{

    private final CartRepository cartRepository;
    private final ProductService productService;

    @Override
    public Cart create(CartCreateRequest req) {
        Product product = productService.findById(req.getProductId());
        Cart cart = req.toEntity(product);

        return cartRepository.save(cart);
    }

    @Override
    public Cart findById(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow();
    }

    @Override
    public Cart addProduct(CartUpdateRequest req) {
        Product product = productService.findById(req.getProductId());
        Cart cart = findById(req.getCartId());

        cart.addProduct(req.getAmount(), product);

        return cart;
    }

    @Override
    public Cart deleteProduct(CartDeleteRequest req) {
        Cart cart = findById(req.getCartId());
        cart.deleteProduct(req.getProductName());

        return cart;
    }
}
