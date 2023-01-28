package com.increff.pos.model;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class OrderData  extends OrderForm{
   private Integer orderId;
   private String orderCreateTime;
    private String orderPlaceTime;
    private String status;
    private Integer counterId;
    private String orderCode;
}
