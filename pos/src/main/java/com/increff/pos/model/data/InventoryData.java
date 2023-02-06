package com.increff.pos.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryData {
    private String productName;
    private String barcode;
    private Integer quantity;
    private Integer productId;

}
