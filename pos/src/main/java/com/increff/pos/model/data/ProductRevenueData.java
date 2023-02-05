package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRevenueData {
    private Integer productId;
    private String productName;
    private String brand;
    private String category;
    private Integer quantity;
    private Double total;

}