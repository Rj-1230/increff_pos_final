package com.increff.pos.model;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class OrderData  extends OrderForm{
   private Integer orderId;
   private ZonedDateTime orderCreateTime;
    private ZonedDateTime orderInvoiceTime;
    private String status;
    private Integer counterId;
    private String orderCode;
}
