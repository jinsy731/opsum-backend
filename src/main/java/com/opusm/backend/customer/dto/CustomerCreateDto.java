package com.opusm.backend.customer.dto;

import com.opusm.backend.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class CustomerCreateDto {

    @Getter
    @AllArgsConstructor
    public static class CustomerCreateRequest {
        private String name;
        private int assets;
        private int points;

        public Customer toEntity(){
            return new Customer(name, points, assets);
        }
    }

    @Getter
    @Setter
    public static class CustomerCreateResponse {
        private Long id;
        private String name;
        private String assets;
        private String points;
        private LocalDateTime createdDate;
    }

}
