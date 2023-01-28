package com.increff.invoice_app.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvoiceForm {
    private Integer orderId;
    private String customerName;
    private String customerPhone;
    private String placeDate;
    private List<OrderItemData> orderItemList;
}