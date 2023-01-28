package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRevenueData {
    private Integer productId;
    private String barcode;
    private String name;
    private String brand;
    private String category;
    private Double mrp;
    private Integer quantity;
    private Double total;

}