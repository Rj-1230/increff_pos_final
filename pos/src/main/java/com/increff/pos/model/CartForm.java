package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartForm {
    private String barcode;
    private Integer quantity;
    private Double sellingPrice;

}
