package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemData {
    private Integer cartItemId;
    private Integer productId;
    private String productName;
    private Double sellingPrice;
    private Integer counterId;
    private Integer quantity;
    private String barcode;

}
