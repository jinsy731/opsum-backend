package com.opusm.backend.cart;

import com.opusm.backend.cart.dto.CartCreateDto;
import com.opusm.backend.cart.dto.CartDeleteDto;
import com.opusm.backend.cart.dto.CartUpdateDto;
import org.springframework.transaction.annotation.Transactional;

import static com.opusm.backend.cart.dto.CartCreateDto.*;
import static com.opusm.backend.cart.dto.CartDeleteDto.*;
import static com.opusm.backend.cart.dto.CartUpdateDto.*;

public interface CartService {

    @Transactional
    Cart create(CartCreateRequest req);

    Cart findById(Long cartId);

    Cart addProduct(CartUpdateRequest req);

    Cart deleteProduct(CartDeleteRequest req);

}
