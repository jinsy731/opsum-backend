package com.opusm.backend.product.dto;

import com.opusm.backend.common.support.convert.ConversionUtils;
import com.opusm.backend.common.support.convert.ModelMappers;
import com.opusm.backend.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ProductCreateDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ProductCreateRequest {
        private String name;
        private int price;
        private float pointRate;
        private int stock;
        private String owner;

        public Product toEntity() {
            return new Product(name, price, pointRate, stock, owner);
        }
    }

    @Getter
    @Setter
    public static class ProductCreateResponse {
        private Long id;
        private String name;
        private int price;
        private float pointRate;
        private int stock;
        private String owner;
        private LocalDateTime createdDate;

        public ProductCreateResponse(Product product) {
            ModelMappers.modelMapper.map(product, this);
        }
    }
}
