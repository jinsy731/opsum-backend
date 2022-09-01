package com.opusm.backend.customer;

import org.springframework.transaction.annotation.Transactional;

public interface CustomerService {
    @Transactional
    Customer create(Customer customer);

    Customer findById(Long customerId);
}
