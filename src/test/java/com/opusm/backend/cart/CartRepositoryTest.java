package com.opusm.backend.cart;

import com.opusm.backend.base.BaseRepositoryTest;
import com.opusm.backend.customer.Customer;
import com.opusm.backend.customer.CustomerRepository;
import com.opusm.backend.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class CartRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CustomerRepository customerRepository;

}