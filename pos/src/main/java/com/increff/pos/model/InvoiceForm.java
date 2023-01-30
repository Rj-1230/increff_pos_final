package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class InvoiceForm {
    private Integer orderId;
    private String customerName;
    private String customerPhone;
    private ZonedDateTime invoiceDate;
    private List<OrderItemData> orderItemList;
}