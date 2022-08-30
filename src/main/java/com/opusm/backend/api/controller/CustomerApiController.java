package com.opusm.backend.api.controller;

import com.opusm.backend.common.support.convert.ConversionUtils;
import com.opusm.backend.customer.Customer;
import com.opusm.backend.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.opusm.backend.customer.dto.CustomerCreateDto.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
@Slf4j
public class CustomerApiController {

    private final CustomerService customerService;

    @PostMapping
    public CustomerCreateResponse create(@RequestBody CustomerCreateRequest req) {
        Customer customer = req.toEntity();
        customerService.create(customer);

        return ConversionUtils.entityToDto(CustomerCreateResponse.class, customer);
    }
}
