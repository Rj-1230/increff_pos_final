package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandRevenueData {
    private String brand;
    private Integer quantity;
    private double totalBrandRevenue;
}