package com.opusm.backend.product.dto;

import com.opusm.backend.common.support.convert.ModelMappers;
import com.opusm.backend.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ProductUpdateDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ProductUpdateRequest {
        private String name;
        private Integer price;
        private Float pointRate;
        private Integer stock;
        private String owner;
    }

    @Getter
    @Setter
    public static class ProductUpdateResponse {
        private Long id;
        private String name;
        private int price;
        private float pointRate;
        private int stock;
        private String owner;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public ProductUpdateResponse(Product product) {
            ModelMappers.modelMapper.map(product, this);
        }
    }
}
