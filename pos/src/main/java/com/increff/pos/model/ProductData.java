package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProductData {
    private Integer productId;

    private String brand;
    private String category;
    private String barcode;
    private String name;
    private Double mrp;

}
