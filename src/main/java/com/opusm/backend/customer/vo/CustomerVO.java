package com.opusm.backend.customer.vo;

import com.opusm.backend.common.support.convert.ModelMappers;
import com.opusm.backend.customer.Customer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CustomerVO {

    private Long id;
    private String name;
    private Integer assets;
    private Integer points;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public CustomerVO(Customer customer) {
        ModelMappers.modelMapper.map(customer, this);
    }

}
