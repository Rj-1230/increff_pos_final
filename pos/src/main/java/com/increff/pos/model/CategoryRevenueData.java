package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CategoryRevenueData {
    private String category;
    private Integer quantity;
    private Double totalCategoryRevenue;
}