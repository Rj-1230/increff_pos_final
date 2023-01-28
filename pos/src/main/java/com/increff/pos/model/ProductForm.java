package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProductForm {
    private String barcode;
    private String brandName;
    private String categoryName;
    private String name;
    private Double mrp;
}
