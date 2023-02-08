package com.increff.invoice_app.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData {
    private Integer orderItemId;
    private Double sellingPrice;
    private String productName;
    private Integer quantity;
}
