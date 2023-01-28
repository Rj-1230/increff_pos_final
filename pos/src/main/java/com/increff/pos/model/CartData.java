package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartData {
    private Integer cartItemId;
    private Integer productId;
    private String productName;
    private Double sellingPrice;
    private Integer counterId;
    private Integer quantity;

}
