package com.opusm.backend.cart;

import com.opusm.backend.cart.dto.CartCreateDto;
import com.opusm.backend.cart.dto.CartDeleteDto;
import com.opusm.backend.cart.dto.CartUpdateDto;
import org.springframework.transaction.annotation.Transactional;

import static com.opusm.backend.cart.dto.CartCreateDto.*;
import static com.opusm.backend.cart.dto.CartDeleteDto.*;
import static com.opusm.backend.cart.dto.CartUpdateDto.*;

public interface CartService {

    Cart findById(Long cartId);

    @Transactional
    Cart addProduct(CartUpdateRequest req);

    @Transactional
    Cart deleteProduct(CartDeleteRequest req);

}
