package com.opusm.backend.cart;

import com.opusm.backend.cart.dto.CartDeleteDto;
import com.opusm.backend.common.exception.ErrorCode;
import com.opusm.backend.common.exception.Preconditions;
import com.opusm.backend.customer.Customer;
import com.opusm.backend.customer.CustomerRepository;
import com.opusm.backend.customer.CustomerService;
import com.opusm.backend.product.Product;
import com.opusm.backend.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.opusm.backend.cart.dto.CartCreateDto.*;
import static com.opusm.backend.cart.dto.CartDeleteDto.*;
import static com.opusm.backend.cart.dto.CartUpdateDto.*;
import static com.opusm.backend.common.exception.Preconditions.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DomainCartService implements CartService{

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CustomerService customerService;

    @Override
    public Cart findById(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow();
    }

    @Override
    public Cart addProduct(CartUpdateRequest req) {
        Product product = productService.findById(req.getProductId());
        Customer customer = customerService.findById(req.getCustomerId());
        Cart cart = customer.getCart();

        validate(req.getAmount() <= product.getStock(), ErrorCode.PRODUCT_NOT_ENOUGH_STOCK);

        cart.addProduct(req.getAmount(), product);
        return cart;
    }

    @Override
    public Cart deleteProduct(CartDeleteRequest req) {
        Customer customer = customerService.findById(req.getCustomerId());
        Cart cart = customer.getCart();
        cart.deleteProduct(req.getProductId());

        return cart;
    }

}
