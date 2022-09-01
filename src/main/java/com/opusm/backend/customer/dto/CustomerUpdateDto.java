package com.opusm.backend.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class CustomerUpdateDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CustomerUpdateRequest {
        private String name;
        private int assets;
        private int points;
    }
}
