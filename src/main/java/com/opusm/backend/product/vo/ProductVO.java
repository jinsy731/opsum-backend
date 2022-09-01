package com.opusm.backend.product.vo;

import lombok.Data;

@Data
public class ProductVO {

    private String name;
    private int price;
    private float pointRate;
    private int stock;
    private String owner;
}
