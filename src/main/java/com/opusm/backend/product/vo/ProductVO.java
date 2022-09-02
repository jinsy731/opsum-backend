package com.opusm.backend.product.vo;

import com.opusm.backend.common.support.convert.ModelMappers;
import com.opusm.backend.product.Product;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductVO {

    private Long id;
    private String name;
    private int price;
    private float pointRate;
    private int stock;
    private String owner;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public ProductVO(Product product) {
        ModelMappers.modelMapper.map(product, this);
    }
}
