package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData {
    private Integer orderItemId;
    private String productName;
    private Double sellingPrice;
    private Integer orderId;
    private Integer productId;

    private Integer quantity;


}
