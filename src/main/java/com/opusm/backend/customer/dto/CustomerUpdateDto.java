package com.opusm.backend.customer.dto;

import com.opusm.backend.common.support.convert.ModelMappers;
import com.opusm.backend.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerUpdateDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CustomerUpdateRequest {
        private String name;
        private Integer assets;
        private Integer points;

        public Customer toEntity() {
            return new Customer(name, assets, points);
        }
    }

    @Getter
    @Setter
    public static class CustomerUpdateResponse {
        private Long id;
        private String name;
        private int assets;
        private int points;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public CustomerUpdateResponse(Customer customer) {
            ModelMappers.modelMapper.map(customer, this);
        }
    }
}
